package kr.co.mindpro.ipms.domain.ai.service.impl;

import kr.co.mindpro.ipms.common.file.dto.response.FileResponse;
import kr.co.mindpro.ipms.common.file.service.FileService;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.ai.dto.response.DocumentAnalysisResponse;
import kr.co.mindpro.ipms.domain.ai.service.AiDocumentAgentService;
import kr.co.mindpro.ipms.domain.ai.service.RagService;
import kr.co.mindpro.ipms.domain.ai.tool.SearchDocumentMasterTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiDocumentAgentServiceImpl implements AiDocumentAgentService {

    private final ChatModel defaultChatModel;
    private final RagService ragService;
    private final SearchDocumentMasterTool documentMasterTool;
    private final FileService fileService;

    @Qualifier("tempTokenCacheManager") // CacheConfig에서 정의한 빈 이름
    private final CacheManager cacheManager;

    private record ExtractedInfo(
            String documentType,
            String searchDocNm,
            String patType,
            String extractedIdentifier,
            String identifierType,
            String summary,
            String direction,
            String attachDocDiv,
            String eventDate
    ) {}

    @Override
    public DocumentAnalysisResponse analyzeAndRegisterDocument(MultipartFile file) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String userSeq = SecurityUtil.getUserInfoSeq();

        try {
            byte[] fileBytes = file.getBytes();
            long fileSize = fileBytes.length;
            log.info("에이전트 처리 시작 - 파일명: {}, 크기: {}", file.getOriginalFilename(), fileSize);

            if (fileSize == 0) {
                throw new RuntimeException("업로드된 파일이 비어 있습니다 (0 bytes).");
            }

            // [1] S3 업로드 및 보안 토큰 생성
            log.info("[Step 1] S3 임시 업로드 시작");
            FileResponse fileRes = fileService.uploadAiTempFile(file, userSeq);
            String realFileSeq = fileRes.fileSeq();
            String fileToken = UUID.randomUUID().toString();
            cacheManager.getCache("tempFileTokens").put(userSeq + ":" + fileToken, realFileSeq);
            log.info("[Step 1] S3 업로드 완료. Token: {}, Seq: {}", fileToken, realFileSeq);

            // [2] 텍스트 추출
            log.info("[Step 2] Tika 텍스트 추출 시작");
            TikaDocumentReader reader = new TikaDocumentReader(new ByteArrayResource(fileBytes, file.getOriginalFilename()));
            String content = reader.get().stream().map(org.springframework.ai.document.Document::getText).collect(Collectors.joining("\n"));
            if (content.length() > 8000) content = content.substring(0, 8000);
            log.info("[Step 2] 텍스트 추출 완료. 길이: {}", content.length());

            // [3] AI 정보 추출
            log.info("[Step 3] AI 정보 추출 프롬프트 전송");
            ChatClient chatClient = ChatClient.builder(defaultChatModel).build();
            String extractionPrompt = String.format("""
            당신은 IPMS(지식재산권 관리 시스템) 전문 분석가입니다. 
            제공된 문서의 텍스트를 분석하여 아래 JSON 형식에 맞춰 모든 데이터를 추출하세요.
            
            [필수 추출 항목]
            1. documentType: 문서의 공식 명칭 (예: 상표우선심사신청서, 등록결정서 등)
            2. searchDocNm: DB 검색을 위한 핵심 명칭 키워드 (예: '등록결정서', '우선심사신청서' 등. '등록'처럼 너무 포괄적인 단어는 피하고 가장 구체적인 명칭을 1개 선택하세요.)
            3. patType: 권리코드 (10:특허, 20:실용, 30:디자인, 40:상표)
            4. extractedIdentifier: 문서 내 출원/등록/사건번호 (하이픈 포함 원문)
            5. summary: 문서 내용을 1~2문장으로 요약
            6. eventDate: 문서 발생일 (YYYYMMDD)
            
            [주의사항]
            - 결과는 반드시 순수 JSON(RFC8259)만 출력하세요.
            - 어떤 설명이나 마크다운 백틱(```json)도 포함하지 마세요.
            - 만약 정보를 찾을 수 없는 필드가 있다면 null 대신 "알 수 없음"이라고 적으세요.
            - 공식 문서가 아니더라도 텍스트 내용을 바탕으로 '무엇에 대한 정보인지' 1~5문장으로 설명하세요.
            내용:
            ---
            %s
            ---
            """, content);

            // AI 호출 및 객체 매핑
            ExtractedInfo info = chatClient.prompt(extractionPrompt).call().entity(ExtractedInfo.class);
            log.info("[Step 3] AI 정보 추출 완료: {}", info);

            // [4] Null 방지 로직
            String docType = (info != null && info.documentType() != null) ? info.documentType() : "분석된 문서";
            String identifier = (info != null && info.extractedIdentifier() != null) ? info.extractedIdentifier() : "번호 확인 불가";
            String summary = (info != null && info.summary() != null) ? info.summary() : "요약 내용을 생성할 수 없습니다.";

            // [5] 후속 조회 (RAG & DB)
            log.info("[Step 5] DB 및 RAG 연관성 조회 시작");
            String realDocSeq = "99";
            if (info != null && StringUtils.hasText(info.searchDocNm())) {
                String toolResult = documentMasterTool.searchDocumentMaster(info.searchDocNm(), info.patType(), null);
                realDocSeq = parseDocSeqFromResult(toolResult);
            }
            String systemSeq = null;
            if (info != null && StringUtils.hasText(info.extractedIdentifier())) {
                String searchResult = ragService.findSimilarDocuments(info.extractedIdentifier(), officeSeq);
                systemSeq = parseRealSequence(searchResult);
            }
            log.info("[Step 5] 조회 완료. systemSeq: {}, docSeq: {}", systemSeq, realDocSeq);

            boolean hasValidIdentifier = StringUtils.hasText(info.extractedIdentifier())
                    && !info.extractedIdentifier().contains("알 수 없음")
                    && !info.extractedIdentifier().contains("확인 불가")
                    && !info.extractedIdentifier().equals("null");

            String displayMessage;

            if (hasValidIdentifier) {
                displayMessage = """
            📄 **%s** 분석을 마쳤습니다.
            
            **추출 번호**: `%s`
            **내용 요약**: %s
            
            해당 건을 시스템에 등록해 드릴까요?
            """.formatted(docType, info.extractedIdentifier(), summary);
            } else {
                displayMessage = """
            📄 업로드하신 문서의 내용을 요약해 드립니다.
            
            **내용 요약**: %s
            
            (식별 번호가 확인되지 않아 별도의 시스템 등록은 지원되지 않습니다.)
            """.formatted(summary);
            }
            // [7] 최종 리턴
            return DocumentAnalysisResponse.builder()
                    .documentType(docType)
                    .extractedIdentifier(identifier)
                    .summary(summary)
                    .systemSeq(systemSeq)
                    .docSeq(realDocSeq)
                    .eventDate(info != null ? info.eventDate() : "")
                    .fileToken(fileToken)
                    .message(displayMessage)
                    .isRegisteredToNextStep(false)
                    .build();

//        } catch (BusinessException e) {
//            throw e;
        } catch (Exception e) {
            log.error("에이전트 처리 오류", e);
            throw new RuntimeException("문서 분석 중 서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // Helper: 결과에서 doc_seq 추출
    private String parseDocSeqFromResult(String result) {
        if (!StringUtils.hasText(result) || result.contains("없습니다")) return "99";
        Pattern pattern = Pattern.compile("doc_seq[:\"\\s]+(\\d+)");
        Matcher matcher = pattern.matcher(result);
        return matcher.find() ? matcher.group(1) : "99";
    }

    // Helper: RAG 결과에서 시스템 시퀀스(PK) 추출
    private String parseRealSequence(String searchResult) {
        if (!StringUtils.hasText(searchResult)) return null;
        Pattern pattern = Pattern.compile("(APPMST|CFTMST|BIZINF|INVMST)[a-zA-Z0-9]{10,15}");
        Matcher matcher = pattern.matcher(searchResult);
        return matcher.find() ? matcher.group() : null;
    }
}