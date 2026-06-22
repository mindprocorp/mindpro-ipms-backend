package kr.co.mindpro.ipms.domain.ai.service;

import kr.co.mindpro.ipms.domain.ai.dto.request.ChatRequest;
import kr.co.mindpro.ipms.domain.ai.vo.AiConnectVo;
import org.springframework.ai.chat.model.ChatModel;
import reactor.core.publisher.Flux;

import java.util.List;

    /**
     * AI 모델 연결 및 대화 로직을 담당하는 서비스 인터페이스
     */
    public interface ModelService {

        /**
         * 1. AI 스트리밍 대화 (RAG 검색 및 프롬프트 구성 포함)
         */
        Flux<String> chatStream(ChatRequest request, String userMstSeq);

        /**
         * 2. AI 일반 질문 (단답형)
         */
        String callQuestion(ChatRequest request, String userMstSeq);

        /**
         * 3. 사내 사건 데이터(이의심판/기타사건) Vector DB 일괄 동기화
         * @return 동기화 요청에 성공한 전체 건수
         */
        int syncAllConflictData(String officeSeq);

        /**
         * 4. 특정 연결 설정에 따른 ChatModel 객체 생성 및 반환
         */
        ChatModel getAiConnect(Long connectionSeq, String userMstSeq);

        /**
         * 5. 사용자가 등록한 AI 모델 연결 목록 조회
         */
        List<AiConnectVo> getAllAiModels(String userMstSeq);

        AiConnectVo createAiModel(AiConnectVo vo);
        AiConnectVo updateAiModel(AiConnectVo vo);
        void deleteAiModel(Long connectionSeq, String userMstSeq);
}