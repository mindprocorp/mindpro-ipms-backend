package kr.co.mindpro.ipms.domain.participant.repository.db1;

import kr.co.mindpro.ipms.domain.conflict.vo.ParticipantSearchVO;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantMergeVo;
import kr.co.mindpro.ipms.domain.participant.vo.ParticipantVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : PaperMapper.java
 * @since	 : 2026. 01. 07.
 */
@Mapper
public interface ParticipantMapper {

    /**
     * 특정 업무(tblSeq)에 연결된 전체 관계자 리스트 조회
     */
    List<ParticipantVO> findParticipantsByWork(@Param("tblSeq") String tblSeq,
                                               @Param("officeSeq") String officeSeq);




    /**
     * [특정 역할 상세 조회용] 특정 역할(예: APP)에 해당하는 '모든' 관계자 리스트 조회
     * (예: 출원인 상세 보기 클릭 시 해당 출원인들만 리스트업)
     */
    List<ParticipantVO> findParticipantsByCode(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq,
            @Param("participantCode") String participantCode); // 코드 필터 추가

    void insertParticipant(ParticipantVO vo);
    /**
     * 기존 관계자 데이터 논리 삭제 (DelYn = 'Y')
     * @return 업데이트된 행 수 (삭제된 데이터가 없어도 0을 반환하여 인서트 로직 보장)
     */
    int updateDeleteStatusByWork(
            @Param("tblSeq") String tblSeq,
            @Param("officeSeq") String officeSeq);

    int updateParticipantDelYnBySeq(
            @Param("participantSeq") String participantSeq,
            @Param("officeSeq") String officeSeq,
            @Param("updateUser") String updateUser
    );

    int updateParticipant(ParticipantVO vo);

    /**
     * [검색용] 다중 역할/이름 조건을 만족하는 tblSeq 리스트 조회 (교집합 검색)
     * @param officeSeq 사무소 코드
     * @param filters   필터 리스트 (participantCode와 검색어 value 포함)
     *
     */
    List<String> findTblSeqsByFilters(
            @Param("officeSeq") String officeSeq,
            @Param("targetJob") String targetJob,
            @Param("filters") List<SearchParamVO> filters
    );


}