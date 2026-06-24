package kr.co.mindpro.ipms.domain.ai.config;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.document.MetadataMode;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAsync
public class AiConfig {

    @Value("${ai.gemini.api-key}")
    private String geminiApiKey;

    @Value("${ai.gemini.model:gemini-2.5-pro}")
    private String geminiModel;

    @Value("${ai.gemini.temperature:0.7}")
    private double geminiTemperature;

    @Value("${ai.openai.api-key}")
    private String openaiApiKey;

    @Value("${ai.openai.model:gpt-4o}")
    private String openaiModel;

    @Value("${ai.embedding.base-url:http://58.233.160.141:11434}")
    private String embeddingBaseUrl;

    @Value("${ai.embedding.model:nomic-embed-text}")
    private String embeddingModelName;

    @Value("${ai.embedding.api-key:ollama}")
    private String embeddingApiKey;

    @Value("${ai.embedding.path:/v1/embeddings}")
    private String embeddingPath;

    /** 임베딩 모델 - 기본값: Google text-embedding-004 (768차원, pgvector 호환) */
    @Bean
    @Primary
    public EmbeddingModel ollamaEmbeddingModel() {
        OpenAiApi embeddingApi = OpenAiApi.builder()
                .baseUrl(embeddingBaseUrl)
                .apiKey(embeddingApiKey)
                .embeddingsPath(embeddingPath)
                .build();

        return new OpenAiEmbeddingModel(
                embeddingApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(embeddingModelName)
                        .build()
        );
    }

    /** * 2. PostgreSQL Vector Store 설정 (핵심 수정)
     * 192.168.0.103 서버의 DB에 벡터 데이터를 저장합니다.
     */
    @Bean
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        String schemaQualifiedTable = "\"MP_IPMS_PA\".vector_store";

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName(schemaQualifiedTable)
                .dimensions(768)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .initializeSchema(false)
                .build();
    }
    /* --- Chat Models (생략 없이 유지) --- */

    @Bean(name = "geminiFlashChatModel")
    public ChatModel geminiFlashChatModel() {
        return createGeminiChatModel(geminiModel);
    }

    @Bean(name = "geminiProChatModel")
    public ChatModel geminiProChatModel() {
        return createGeminiChatModel("gemini-2.5-pro");
    }

    @Bean(name = "gemini2FlashChatModel")
    public ChatModel gemini2FlashChatModel() {
        return createGeminiChatModel("gemini-2.5-flash");
    }

    @Bean(name = "geminiFlashLiteChatModel")
    public ChatModel geminiFlashLiteChatModel() {
        return createGeminiChatModel("gemini-2.5-flash");
    }

    private ChatModel createGeminiChatModel(String model) {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai")
                .defaultHeader("Authorization", "Bearer " + geminiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        return new GeminiChatModel(restClient, model, geminiTemperature);
    }

    @Bean(name = "openaiGpt4oChatModel")
    public ChatModel openaiGpt4oChatModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl("https://api.openai.com/v1").apiKey(openaiApiKey).build())
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").temperature(0.7).build())
                .build();
    }

    @Bean(name = "openaiGpt4oMiniChatModel")
    public ChatModel openaiGpt4oMiniChatModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl("https://api.openai.com/v1").apiKey(openaiApiKey).build())
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o-mini").temperature(0.7).build())
                .build();
    }

    @Bean(name = "azureOpenAiChatModel")
    public ChatModel azureOpenAiChatModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl("YOUR_AZURE_OPENAI_ENDPOINT").apiKey("YOUR_AZURE_API_KEY").build())
                .defaultOptions(OpenAiChatOptions.builder().model("YOUR_AZURE_DEPLOYMENT_NAME").temperature(0.7).build())
                .build();
    }

    @Bean(name = "claudeChatModel")
    public ChatModel claudeChatModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder().baseUrl("https://api.anthropic.com/v1").apiKey("YOUR_ANTHROPIC_API_KEY").build())
                .defaultOptions(OpenAiChatOptions.builder().model("claude-3-5-sonnet-20241022").temperature(0.7).build())
                .build();
    }

    @Bean(name = "defaultChatModel")
    @Primary
    public ChatModel defaultChatModel() {
        return geminiFlashChatModel();
    }

    /**
     * 사용자 등록 Gemini 모델 생성 팩토리.
     * OpenAiApi는 baseUrl에 /v1/chat/completions를 붙이므로 Gemini 경로와 맞지 않아
     * RestClient 기반의 GeminiChatModel을 직접 생성합니다.
     */
    public ChatModel createUserGeminiChatModel(String apiKey, String baseUrl, String model, double temperature) {
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
        return new GeminiChatModel(restClient, model, temperature);
    }

    /* --- Gemini API 전용 내부 클래스 (유지) --- */

    private static class GeminiChatModel implements ChatModel, StreamingChatModel {
        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GeminiChatModel.class);

        private static final List<String> FALLBACK_MODELS = List.of(
                "gemini-2.5-flash"
        );
        private static final int    MAX_RETRY_PER_MODEL = 3;
        private static final long   INITIAL_BACKOFF_MS  = 1000L;

        private final RestClient restClient;
        private final String model;
        private final double temperature;

        public GeminiChatModel(RestClient restClient, String model, double temperature) {
            this.restClient = restClient;
            this.model = model;
            this.temperature = temperature;
        }

        @Override
        public ChatResponse call(Prompt prompt) {
            List<GeminiMessage> messages = new ArrayList<>();
            for (var msg : prompt.getInstructions()) {
                messages.add(new GeminiMessage(msg.getMessageType().name().toLowerCase(), msg.getText()));
            }

            List<String> modelChain = new ArrayList<>();
            modelChain.add(model);
            for (String fb : FALLBACK_MODELS) {
                if (!fb.equals(model)) modelChain.add(fb);
            }

            RuntimeException lastError = null;
            for (String currentModel : modelChain) {
                try {
                    GeminiResponse response = callWithRetry(currentModel, messages);
                    if (response == null || response.choices() == null || response.choices().length == 0) {
                        return new ChatResponse(List.of(new Generation(new AssistantMessage("응답이 없습니다."), null)), null);
                    }
                    if (!currentModel.equals(model)) {
                        log.warn("Gemini 메인 모델({}) 실패 → 폴백 모델({})로 응답 성공", model, currentModel);
                    }
                    String content = response.choices()[0].message().content();
                    return new ChatResponse(List.of(new Generation(new AssistantMessage(content), null)), null);
                } catch (RuntimeException ex) {
                    lastError = ex;
                    log.warn("Gemini 모델({}) 호출 실패: {}", currentModel, ex.getMessage());
                }
            }
            String msg = "AI 서비스가 일시적으로 응답할 수 없습니다. 잠시 후 다시 시도해주세요.";
            log.error("Gemini 전체 모델 체인 실패", lastError);
            return new ChatResponse(List.of(new Generation(new AssistantMessage(msg), null)), null);
        }

        /** 단일 모델에 대해 지수 백오프 재시도 */
        private GeminiResponse callWithRetry(String currentModel, List<GeminiMessage> messages) {
            var request = new GeminiRequest(currentModel, temperature, messages);
            RuntimeException last = null;
            long backoff = INITIAL_BACKOFF_MS;

            for (int attempt = 1; attempt <= MAX_RETRY_PER_MODEL; attempt++) {
                try {
                    return restClient.post()
                            .uri("/chat/completions")
                            .body(request)
                            .retrieve()
                            .body(GeminiResponse.class);
                } catch (HttpServerErrorException ex) {
                    last = ex;
                } catch (HttpClientErrorException.TooManyRequests ex) {
                    last = ex;
                } catch (ResourceAccessException ex) {
                    last = ex;
                } catch (HttpClientErrorException ex) {
                    throw ex;
                }

                if (attempt < MAX_RETRY_PER_MODEL) {
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    backoff *= 2;
                }
            }
            throw last != null ? last : new RuntimeException("Gemini 호출 실패");
        }

        @Override
        public reactor.core.publisher.Flux<String> stream(String text) {
            return reactor.core.publisher.Flux.just(call(new Prompt(text)).getResults().get(0).getOutput().getText());
        }

        @Override
        public reactor.core.publisher.Flux<ChatResponse> stream(Prompt prompt) {
            return stream(prompt.getInstructions().get(0).getText())
                    .map(content -> new ChatResponse(List.of(new Generation(new AssistantMessage(content), null)), null));
        }
    }

    private record GeminiRequest(String model, double temperature, List<GeminiMessage> messages) {}
    private record GeminiMessage(String role, String content) {}
    private record GeminiResponse(GeminiChoice[] choices) {}
    private record GeminiChoice(GeminiMessage message) {}
}