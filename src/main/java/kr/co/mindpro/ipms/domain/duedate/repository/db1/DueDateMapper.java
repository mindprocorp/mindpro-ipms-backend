package kr.co.mindpro.ipms.domain.duedate.repository.db1;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.conflict.vo.DateSearchVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateManageVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateMapVO;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.duedate.vo.ProgressHistoryVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 청구서 데이터 접근 인터페이스
 * MyBatis XML과 연동되어 SQL을 실행합니다.
 *
 * @author	 : min
 * @fileName	 : DuedateMapper.java
 * @since	 : 2026. 01. 07.
 */
/**
 * 기일 관리 데이터 접근 인터페이스
 */
@Mapper
public interface DueDateMapper {

    /**
     * [조회] 업무별 전체 기일 리스트 조회 (정규화된 List 반환)
     */
    List<DueDateVO> findAllByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [삭제 1] 기일 마스터(Mst) 논리 삭제
     */
    int deleteMstByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [삭제 2] 기일 매핑(Mapp) 논리 삭제
     */
    int deleteMappByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [삭제 and 인서트] softDelete(update) 이후 해당 키 값으로 다시 인서트 작업. (WITH, RETURNING 사용)
     * */
    int softDeleteAndInsertDueDate(DueDateVO vo);

    /**
     * [조회] 업무키에 연결된 기일정보의 개수를 조회합니다.
     * */
    String getDuedateSeqByWork(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [저장] 기일 통합 등록 (Mst + Mapp 일괄 처리)
     */
    void insertDueDate(DueDateVO vo);

    /**
     * [검색] 다양한 조건으로 기일 조회 (기존 유지)
     */
    List<DueDateManageVO> findDueDateList(@Param("request") BaseSearchRequest request);

    /**
     * [대시보드용] 오늘 이후 일정 조회 (기존 유지)
     */
    List<DueDateVO> findUpcomingDueDates(@Param("officeSeq") String officeSeq);

    /**
     * [검색용] 여러 날짜 조건을 만족하는 tblSeq 리스트 조회
     * @param officeSeq 사무소 코드
     * @param dateFilters 날짜 코드, 시작일, 종료일을 담은 리스트
     */
    List<String> findTblSeqsByDateFilters(
            @Param("officeSeq") String officeSeq,
            @Param("targetJob") String targetJob,
            @Param("dateFilters") List<SearchParamVO> dateFilters
    );


    int insertDueDateMapp(DueDateMapVO vo);

    /**
     * [삭제] 특정 업무키의 duedate_mapp 레코드 삭제 (재등록 전 정리용)
     */
    void deleteDueDateMappByTblSeq(@Param("tblSeq") String tblSeq, @Param("officeSeq") String officeSeq);

    /**
     * [수정] 기일 완료 여부 업데이트 (Y/N)
     */
    int updateCompleteYn(@Param("duedateSeq") String duedateSeq,
                         @Param("completeYn") String completeYn,
                         @Param("updateUser") String updateUser,
                         @Param("dueTypeCategoryCode") String dueTypeCategoryCode);


    //접발송내역

    List<ProgressHistoryVO> selectProgressHistoryList( @Param("request")BaseSearchRequest request);

    int selectProgressHistoryCount(@Param("request")BaseSearchRequest request);

    int findDueDateListCount(@Param("request")BaseSearchRequest request);
}