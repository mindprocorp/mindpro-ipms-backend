package kr.co.mindpro.ipms.domain.participant.service;

import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;

import java.util.List;
import java.util.Map;

/**
 * [Service Interface] 관계자 관리 서비스
 *
 * @author   : min
 * @fileName : ParticipantService.java
 * @since    : 2026. 01. 07.
 */
public interface ParticipantService {

    /**
     * [Detail 요약용] 각 역할별 대표자(main_yn='Y') 성명 Map 조회
     * (백엔드 타 업무 참조 및 상세페이지 상단 UI용)
     */
    //ParticipantDetailResponse getParticipantListByWork(String tblSeq, String officeSeq);
    List<ParticipantVO> getParticipantListByWork(String tblSeq, String officeSeq);

    /**
     * [특정 역할 전체 조회] 특정 코드(예: APP)에 해당하는 모든 관계자 리스트 조회
     * (대표자 외 다른 관계자들을 상세히 보고 싶을 때 호출)
     */
    List<ParticipantVO> getParticipantsByCode(String tblSeq, String officeSeq, String participantCode);

    /**
     * 관계자 정보 일괄 저장 (기존 데이터 논리 삭제 후 재등록)
     */
    void saveAllParticipants(List<ParticipantVO> vo);

    /**
     * 관계자 단건 등록
     */
    void insertParticipant(ParticipantVO vo);



    //List<String> getIdsByFilters(String officeSeq, List<ParticipantSearchVO> list);
    List<String> getIdsByFilters(String officeSeq, String targetJob, List<SearchParamVO> list);
}

