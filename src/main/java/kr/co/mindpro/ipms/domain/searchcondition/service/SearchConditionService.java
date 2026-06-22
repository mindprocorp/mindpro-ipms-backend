package kr.co.mindpro.ipms.domain.searchcondition.service;


import kr.co.mindpro.ipms.domain.searchcondition.dto.request.SearchConditionRequest;
import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionListResponse;
import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionResponse;

import java.util.List;

/**
 * [Service Interface] 이의심판 관리 서비스
 *
 * @author   : min
 * @fileName : ParticipantService.java
 * @since    : 2026. 01. 07.
 */
public interface SearchConditionService {

    /**
     * 검색 조건 저장
     * @param request (searchOptions, dateFilters, textFilters 포함)
     * @return 생성된 conditionSeq
     */
    String saveSearchCondition(SearchConditionRequest request);

    /**
     * 메뉴별 검색 조건 목록 조회 (바인딩 데이터 포함)
     * @param menuCode (예: CONFLICT, APP)
     * @return SearchConditionListResponse (List<SearchConditionResponse>와 totalCount 포함)
     */
    SearchConditionListResponse getSearchConditionList(String menuCode);

    /**
     * 특정 시퀀스로 검색 조건 상세 조회
     * @param conditionSeq
     * @return SearchConditionResponse
     */
    SearchConditionResponse getSearchConditionDetail(String conditionSeq);

    /**
     * 검색 조건 삭제 (논리 삭제)
     * @param conditionSeq
     * @return 삭제된 행의 수 (int)
     */
    int deleteSearchCondition(String conditionSeq);
}