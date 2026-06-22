package kr.co.mindpro.ipms.domain.ai.service.impl;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ai.config.AiConfig;
import kr.co.mindpro.ipms.domain.ai.dto.request.ChatRequest;
import kr.co.mindpro.ipms.domain.ai.repository.db3.ModelMapper;
import kr.co.mindpro.ipms.domain.ai.service.ModelService;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.ai.util.SkillRegistry;
import kr.co.mindpro.ipms.domain.ai.vo.AiConnectVo;
import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeoutException;


import java.util.List;
import kr.co.mindpro.ipms.domain.ai.util.AiPromptManager;
import kr.co.mindpro.ipms.domain.ai.tool.*;
import org.springframework.ai.chat.client.ChatClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelMapper modelMapper;
    private final RagService ragService;
    private final ConflictService conflictService;
    private final AiPromptManager aiPromptManager;
    private final SkillRegistry skillRegistry;
    private final AiConfig aiConfig;

    private final ChatModel defaultChatModel;
    private final ChatModel geminiFlashChatModel;
    private final ChatModel geminiProChatModel;
    private final ChatModel gemini2FlashChatModel;
    private final ChatModel openaiGpt4oChatModel;
    private final ChatModel openaiGpt4oMiniChatModel;
    private final ChatModel azureOpenAiChatModel;
    private final ChatModel claudeChatModel;

    @Override
    public Flux<String> chatStream(ChatRequest request, String userMstSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userInfoSeq = SecurityUtil.getUserInfoSeq();
        ChatModel model = this.getAiConnect(Long.valueOf(request.aiCode()), userMstSeq);
        String context = ragService.findSimilarDocuments(request.message(), officeSeq);

        String currentDate = java.time.LocalDate.now().toString();
        String intent = aiPromptManager.getIntent(request.message());
        String systemPrompt = aiPromptManager.getSystemPrompt(userInfoSeq, currentDate, officeSeq,
                (request.history() != null ? request.history() : "없음"), context, request.message(), userMstSeq);

        Object[] tools = skillRegistry.getToolsForIntent(intent);

        ChatClient chatClient = ChatClient.builder(model)
                .defaultTools(tools)
                .build();

        // BUSINESS / KIPRIS 인텐트는 tool call 후 두 번째 스트리밍 응답이 빈 content로
        // 반환되는 Gemini 호환 이슈가 있으므로 동기 call()로 처리합니다.
        // KIPRIS는 외부 API 다중 호출 가능성이 있어 타임아웃을 더 길게 설정합니다.
        if ("BUSINESS".equals(intent) || "KIPRIS".equals(intent)) {
            int timeoutSec = "KIPRIS".equals(intent) ? 90 : 60;
            return Mono.fromCallable(() -> {
                        String response = chatClient.prompt().system(systemPrompt).user(request.message()).call().content();
                        return (response != null && !response.isBlank()) ? response : "처리가 완료되었습니다.";
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .timeout(Duration.ofSeconds(timeoutSec))
                    .onErrorResume(TimeoutException.class, e -> {
                        log.warn("[{}] AI 응답 타임아웃 ({}초 초과)", intent, timeoutSec);
                        return Mono.just("⏱️ 응답 시간이 초과되었습니다 (" + timeoutSec + "초). 잠시 후 다시 시도해 주세요.");
                    })
                    .onErrorResume(e -> {
                        log.error("[{}] AI 처리 오류", intent, e);
                        return Mono.just("처리 중 오류가 발생했습니다. 담당자에게 문의해 주세요.");
                    })
                    .flux();
        }

        return chatClient.prompt().system(systemPrompt).user(request.message()).stream().content()
                .onErrorResume(e -> {
                    log.error("AI 스트리밍 호출 실패", e);
                    return Flux.just(friendlyErrorMessage(e));
                });
    }

    @Override
    public String callQuestion(ChatRequest request, String userMstSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userInfoSeq = SecurityUtil.getUserInfoSeq();
        ChatModel model = this.getAiConnect(Long.valueOf(request.aiCode()), userMstSeq);
        String context = ragService.findSimilarDocuments(request.message(), officeSeq);

        String currentDate = java.time.LocalDate.now().toString();
        String systemPrompt = aiPromptManager.getSystemPrompt(userInfoSeq, currentDate, officeSeq, 
                (request.history() != null ? request.history() : "없음"), context, request.message(), userMstSeq);

        Object[] tools = skillRegistry.getToolsForIntent(aiPromptManager.getIntent(request.message()));

        ChatClient chatClient = ChatClient.builder(model)
                .defaultTools(tools)
                .build();

        try {
            return chatClient.prompt().system(systemPrompt).user(request.message()).call().content();
        } catch (Exception e) {
            log.error("AI 동기 호출 실패", e);
            return friendlyErrorMessage(e);
        }
    }

    /**
     * 외부 AI 호출 예외를 사용자 친화 메시지로 변환.
     * - 503/UNAVAILABLE: 모델 과부하
     * - 429: 할당량/요청 한도
     * - 401/403: 인증 문제
     * - 그 외: 일반 오류
     */
    private String friendlyErrorMessage(Throwable e) {
        String msg = e.getMessage() == null ? "" : e.getMessage();
        if (msg.contains("503") || msg.contains("Service Unavailable") || msg.contains("UNAVAILABLE")) {
            return "AI 모델이 일시적으로 과부하 상태입니다. 잠시 후 다시 시도해주세요.";
        }
        if (msg.contains("429") || msg.contains("Too Many Requests")) {
            return "요청량이 많아 잠시 제한되었습니다. 1~2분 후 다시 시도해주세요.";
        }
        if (msg.contains("401") || msg.contains("403")) {
            return "AI 인증 정보가 올바르지 않습니다. 모델 설정을 확인해주세요.";
        }
        if (msg.contains("404")) {
            return "지정한 AI 모델을 찾을 수 없습니다. 모델 설정을 확인해주세요.";
        }
        return "AI 응답 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    }

    @Override
    public int syncAllConflictData(String officeSeq) {
        int count = 0;
        // SearchRequest를 빌더로 생성하는 것은 BaseSearchRequest 규격에 따름
        BaseSearchRequest req = BaseSearchRequest.builder().pageSize(99999).build();

        var conflictList = conflictService.getConflictList(req);
        if (conflictList.getList() != null) {
            for (var item : conflictList.getList()) {
                try {
                    var detail = conflictService.getConflictDetail(item.conflictSeq());
                    ragService.syncVectorData(officeSeq, "CONFLICT", String.valueOf(item.conflictSeq()),"심판",  detail);
                    count++;
                } catch (Exception e) { log.error("에러: {}", item.conflictSeq()); }
            }
        }
        return count;
    }

    @Override
    public ChatModel getAiConnect(Long connectionSeq, String userMstSeq) {
        if (connectionSeq == null || connectionSeq == 0L) return defaultChatModel;

        var config = modelMapper.findByConnectionSeq(connectionSeq, userMstSeq)
                .orElseThrow(() -> new RuntimeException("AI Model configuration not found for sequences: " + connectionSeq));

        // 1. BaseUrl이 있으면 동적 연결 (Ollama 등 로컬 LLM은 API Key 없이도 동작)
        if (config.getAiBaseUrl() != null && !config.getAiBaseUrl().trim().isEmpty()) {
            return createCustomChatModel(config);
        }

        // 2. 직접 입력된 Key가 없으면 기본 Autowired 된 글로벌 Bean으로 폴백
        String aiType = config.getAiType();
        if (aiType != null) {
            return switch (aiType.toUpperCase()) {
                case "GEMINI_FLASH" -> geminiFlashChatModel;
                case "GEMINI_PRO" -> geminiProChatModel;
                case "GEMINI_2_FLASH" -> gemini2FlashChatModel;
                case "OPENAI_GPT4O" -> openaiGpt4oChatModel;
                case "OPENAI_GPT4O_MINI" -> openaiGpt4oMiniChatModel;
                case "AZURE_OPENAI" -> azureOpenAiChatModel;
                case "CLAUDE" -> claudeChatModel;
                default -> defaultChatModel;
            };
        }
        
        return defaultChatModel;
    }

    private ChatModel createCustomChatModel(AiConnectVo config) {
        // DB에는 사용자가 입력한 그대로 저장 - 여기서 타입별로 경로를 처리
        String rawUrl = config.getAiBaseUrl().stripTrailing().replaceAll("/+$", "");
        String apiKey = (config.getAiApiKey() != null && !config.getAiApiKey().trim().isEmpty())
                ? config.getAiApiKey() : "local";
        double temperature = config.getAiTemperature() != null ? config.getAiTemperature() : 0.7;
        String type = config.getAiType() != null ? config.getAiType().toUpperCase() : "";

        // GEMINI: RestClient 기반 팩토리 사용 (OpenAiApi는 /v1/chat/completions를 붙여 경로 충돌)
        // baseUrl은 /v1beta/openai로 끝나야 함
        if ("GEMINI".equals(type)) {
            String geminiUrl = rawUrl.endsWith("/v1beta/openai") ? rawUrl
                    : rawUrl.endsWith("/v1beta") ? rawUrl + "/openai"
                    : rawUrl + "/v1beta/openai";
            return aiConfig.createUserGeminiChatModel(apiKey, geminiUrl, config.getAiModelNm(), temperature);
        }

        // OPENAI / OLLAMA / AZURE: OpenAiApi가 /v1/chat/completions를 자동으로 붙임
        // → baseUrl에 /v1이 이미 있으면 중복되므로 제거 (이전에 /v1이 붙어 저장된 레거시 데이터 대응)
        String baseUrl = rawUrl.endsWith("/v1") ? rawUrl.substring(0, rawUrl.length() - 3) : rawUrl;

        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl(baseUrl)
                        .apiKey(apiKey)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(config.getAiModelNm())
                        .temperature(temperature)
                        .build())
                .retryTemplate(buildAiRetryTemplate())
                .build();
    }

    /**
     * 503/429/네트워크 타임아웃 등 일시적 장애에 대해 지수 백오프 재시도.
     * - 최대 4회 (초기 1회 + 재시도 3회)
     * - 백오프: 1s → 2s → 4s (multiplier 2.0, max 10s)
     * - 4xx (잘못된 요청) 은 재시도하지 않고 즉시 예외 전파
     */
    private RetryTemplate buildAiRetryTemplate() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(HttpServerErrorException.class, true);              // 5xx
        retryableExceptions.put(HttpClientErrorException.TooManyRequests.class, true); // 429
        retryableExceptions.put(ResourceAccessException.class, true);               // 타임아웃/네트워크
        // HttpClientErrorException (4xx) 은 retryable=false (등록 안 함)

        SimpleRetryPolicy policy = new SimpleRetryPolicy(4, retryableExceptions, true);

        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10_000L);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(policy);
        template.setBackOffPolicy(backOff);
        template.registerListener(new org.springframework.retry.RetryListener() {
            @Override
            public <T, E extends Throwable> void onError(
                    org.springframework.retry.RetryContext context,
                    org.springframework.retry.RetryCallback<T, E> callback,
                    Throwable throwable) {
                log.warn("AI 호출 재시도 [{}회차]: {}", context.getRetryCount(), throwable.getMessage());
            }
        });
        return template;
    }

    /**
     * AI 타입별 base URL을 올바른 엔드포인트 경로로 정규화합니다.
     * - GEMINI: /v1beta/openai 경로 필요 (Google OpenAI 호환 엔드포인트)
     * - OPENAI_*: /v1 경로 필요 (OpenAI 표준 엔드포인트)
     */
    private String normalizeBaseUrl(String aiType, String baseUrl) {
        if (aiType == null || baseUrl == null) return baseUrl;
        String normalized = baseUrl.stripTrailing().replaceAll("/+$", "");
        String type = aiType.toUpperCase();

        if ("GEMINI".equals(type)) {
            if (!normalized.endsWith("/v1beta/openai")) {
                if (normalized.endsWith("/v1beta")) return normalized + "/openai";
                return normalized + "/v1beta/openai";
            }
        } else if (type.startsWith("OPENAI") || "AZURE_OPENAI".equals(type) || "LOCAL".equals(type)) {
            // OpenAI 표준 엔드포인트는 /v1 필요 (Ollama는 사용자가 입력한 그대로 사용)
            if (!normalized.endsWith("/v1") && !normalized.contains("/openai/deployments/")) {
                return normalized + "/v1";
            }
        }
        return normalized;
    }

    @Override
    public List<AiConnectVo> getAllAiModels(String userMstSeq) {
        return modelMapper.findAllByUserMstSeq(userMstSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiConnectVo createAiModel(AiConnectVo vo) {
        String loginUserMstSeq = SecurityUtil.getUserMstSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        vo.setUserMstSeq(loginUserMstSeq);
        vo.setCreateUser(loginUser);
        vo.setUpdateUser(loginUser);
        // GEMINI/OPENAI 타입은 base URL을 올바른 경로로 자동 정규화
        vo.setAiBaseUrl(normalizeBaseUrl(vo.getAiType(), vo.getAiBaseUrl()));
        modelMapper.insertModel(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiConnectVo updateAiModel(AiConnectVo vo) {
        String loginUserMstSeq = SecurityUtil.getUserMstSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();
        vo.setUserMstSeq(loginUserMstSeq);
        vo.setUpdateUser(loginUser);
        // GEMINI/OPENAI 타입은 base URL을 올바른 경로로 자동 정규화
        vo.setAiBaseUrl(normalizeBaseUrl(vo.getAiType(), vo.getAiBaseUrl()));
        modelMapper.updateModel(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiModel(Long connectionSeq, String userMstSeq) {
        String loginUser = kr.co.mindpro.ipms.common.util.SecurityUtil.getUserInfoSeq();
        modelMapper.deleteModel(connectionSeq, userMstSeq, loginUser);
    }
}