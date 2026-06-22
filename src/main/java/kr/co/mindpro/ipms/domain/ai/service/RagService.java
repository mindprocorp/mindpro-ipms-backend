package kr.co.mindpro.ipms.domain.ai.service;

import java.util.List;

public interface RagService {
    
    /**
     * 특정 텍스트 데이터를 벡터화(임베딩)하여 Vector DB에 저장합니다.
     */
    void addDocument(String officeSeq, String content, String source);
    
    /**
     * 사용자의 질문과 유사한 문서를 Vector DB에서 검색합니다.
     */
    //String findSimilarDocuments(String query);

    /**
     * 벡터 DB 데이터 동기화
     * @param officeSeq  고객사 번호
     * @param domainType 시스템 관리용 코드 (예: PATENT_APP, CONFLICT)
     * @param uniqueId   데이터 고유 식별자 (PK)
     * @param domainNm   AI가 인식할 한글 도메인명 (예: "국내 출원 마스터", "심판/분쟁")
     * @param data       VO 또는 Map 데이터
     */
    void syncVectorData(String officeSeq, String domainType, String uniqueId, String domainNm, Object data);

    String findSimilarDocuments(String query, String officeSeq);
}
