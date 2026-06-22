package kr.co.mindpro.ipms.domain.ai.service.impl;

import kr.co.mindpro.ipms.domain.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import kr.co.mindpro.ipms.domain.ai.util.VectorDocumentParser;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final VectorStore vectorStore;
    private static final String VECTOR_STORE_PATH = "vector_store.json";

    @Override
    @Async
    public void syncVectorData(String officeSeq, String domainType, String uniqueId, String domainNm, Object data) {
        if (data == null) return;

        try {
            // 1. 비즈니스 ID 생성 및 결정적 UUID 변환
            String businessId = domainType.toUpperCase() + "_" + uniqueId;
            String vectorId = java.util.UUID.nameUUIDFromBytes(businessId.getBytes()).toString();

            // 2. [핵심] 파서를 호출할 때 AI용 도메인 한글명을 전달 (라벨링 작업)
            // VectorDocumentParser.parseToText(data, "국내 출원 마스터") 형태가 됨
            String text = VectorDocumentParser.parseToText(data, domainNm);

            // 3. 메타데이터 구성
            Map<String, Object> metadata = new java.util.HashMap<>();
            metadata.put("source", domainType);         // 관리용 (PATENT_APP 등)
            metadata.put("domainName", domainNm);      // AI 참조용 (국내 출원 마스터 등)
            metadata.put("business_id", businessId);
            metadata.put("officeSeq", officeSeq != null ? officeSeq : "UNKNOWN");

            // 모든 필드 데이터를 JSON으로 변환하여 메타데이터에 보관 (추후 정밀 분석용)
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                // Java 8 Date/Time 지원 활성화 (필요 시)
                mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                String jsonData = mapper.writeValueAsString(data);
                metadata.put("allFields", jsonData);
            } catch (Exception e) {
                log.debug(">>>> [AI RAG] JSON 변환 실패: {}", e.getMessage());
            }

            // 4. 벡터 데이터 생성 및 저장
            if (StringUtils.hasText(text)) {
                Document doc = Document.builder()
                        .id(vectorId)
                        .text(text)
                        .metadata(metadata)
                        .build();

                // PgVectorStore의 add는 내부적으로 Upsert(Insert or Update)를 수행함
                vectorStore.add(List.of(doc));
                log.info(">>>> [AI RAG] VectorStore 동기화 완료! (유형: {}, ID: {})", domainNm, businessId);
            }

        } catch (Exception e) {
            log.error(">>>> [AI RAG] 동기화 중 치명적 오류: ", e);
        }
    }

    @Override
    public void addDocument(String officeSeq, String content, String source) {
        if (content != null && !content.trim().isEmpty()) {
            Document doc = Document.builder()
                    .text(content)
                    .metadata(Map.of("source", source, "officeSeq", officeSeq != null ? officeSeq : "UNKNOWN"))
                    .build();
            vectorStore.add(List.of(doc));
            this.saveToFile();
        }
    }

    @Override
    public String findSimilarDocuments(String query, String officeSeq) {
        log.info("RAG 정밀 검색 실행 - ID: {}, Office: {}", query, officeSeq);

        // 1. 필터 구성: 업무 진행사항(JOB_PROGRESS)은 제외하고 마스터 정보만 타겟팅
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression filterExpression = b.and(
                b.eq("officeSeq", officeSeq),
                b.ne("source", "JOB_PROGRESS")
        ).build();

        // 2. 검색 실행: 상위 5개까지 확보 (유형 중복 대비)
        List<Document> result = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(5)
                        .similarityThreshold(0.4) // 검색 감도 조절
                        .filterExpression(filterExpression)
                        .build()
        );

        if (result.isEmpty()) {
            return "일치하는 사건 정보가 없습니다.";
        }

        // 3. 자바 단에서 최종 검증: 검색된 텍스트에 입력한 번호(query)가 그대로 포함되어 있는지 체크
        String filteredResults = result.stream()
                .filter(doc -> doc.getText().contains(query)) // 하이픈 포함 원문 그대로 비교
                .map(doc -> {
                    // 이전에 VectorDocumentParser에서 넣은 domainName 메타데이터 활용
                    String domainName = (String) doc.getMetadata().getOrDefault("domainName", "사건 마스터");
                    return String.format("=== [데이터 유형: %s] ===\n%s", domainName, doc.getText());
                })
                .collect(Collectors.joining("\n\n"));

        // 최종 결과 반환
        return StringUtils.hasText(filteredResults)
                ? filteredResults
                : "검색 결과 중 입력하신 번호('" + query + "')와 정확히 일치하는 정보가 없습니다.";
    }

    private void saveToFile() {
        if (vectorStore instanceof SimpleVectorStore simpleStore) {
            try {
                simpleStore.save(new File(VECTOR_STORE_PATH));
            } catch (Exception e) {
                log.error("저장 실패", e);
            }
        }
    }
}