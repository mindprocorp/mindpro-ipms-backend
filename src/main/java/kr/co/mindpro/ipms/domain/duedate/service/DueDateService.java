package kr.co.mindpro.ipms.domain.duedate.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.conflict.vo.DateSearchVO;
import kr.co.mindpro.ipms.domain.conflict.vo.ParticipantSearchVO;
import kr.co.mindpro.ipms.domain.duedate.dto.request.DueDateSaveRequest;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateDetailResponse;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateResponse;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchParamVO;

import java.util.List;
import java.util.Map;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : InvoiceService.java
 * @since    : 2026. 01. 07.
 */

public interface DueDateService {


        /** [저장] 화면에서 넘어온 기일 맵을 순회하며 등록/수정 (Upsert) */
        void saveAllDueDates(List<DueDateVO> list);

        /** [저장] 단건 기일 등록 */
        void registerDueDate(DueDateVO vo);

        /** [조회] 특정 업무의 기일 정보를 Map 구조로 반환 (상세 화면용) */
        //DueDateDetailResponse getDueDateMapByWork(String tblSeq, String officeSeq);

        List<DueDateVO> getDueDateListByWork(String tblSeq, String officeSeq);
        /** [조회] 기일 리스트 검색 */
        BaseSearchResponse<DueDateResponse.DueDateDetail> getDueDateList(BaseSearchRequest request);

        /** [조회] 다가오는 일정 및 미완료 기일 조회 */
        List<DueDateVO> getUpcomingDueDates(String officeSeq);

        List<String> getIdsByDateFilters(String officeSeq, String targetJob ,List<SearchParamVO> dateFilters);


        BaseSearchResponse<DueDateResponse.ProgressHistoryDetail> getProgressHistoryList(BaseSearchRequest request);

        /** [수정] 기일 완료 여부 변경 (Y/N) */
        void updateCompleteYn(String duedateSeq, String completeYn, String dueTypeCategoryCode);

        /** [삭제] 특정 업무키의 duedate_mapp 삭제 (재등록 전 정리용) */
        void deleteDueDateMappByTblSeq(String tblSeq, String officeSeq);
}