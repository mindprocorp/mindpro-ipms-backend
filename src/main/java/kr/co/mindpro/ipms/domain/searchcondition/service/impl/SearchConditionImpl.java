package kr.co.mindpro.ipms.domain.searchcondition.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.util.SecurityUtil;

import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionListResponse;
import kr.co.mindpro.ipms.domain.searchcondition.repository.db1.SearchConditionMapper;
import kr.co.mindpro.ipms.domain.searchcondition.dto.request.SearchConditionRequest;
import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionResponse;
import kr.co.mindpro.ipms.domain.searchcondition.service.SearchConditionService;
import kr.co.mindpro.ipms.domain.searchcondition.vo.SearchConditionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 청구서 비즈니스 로직 구현체
 * 
 * @author   : min
 * @fileName : PaperServiceImpl.java
 * @since    : 2026. 01. 07.
 */
@Service
@RequiredArgsConstructor
public class SearchConditionImpl implements SearchConditionService {
    private final SearchConditionMapper searchConditionMapper;
    private final ObjectMapper objectMapper;

    /**
     * 1. 검색 조건 저장
     */
    @Override
    @Transactional
    public String saveSearchCondition(SearchConditionRequest request) {
        String userSeq = SecurityUtil.getUserInfoSeq();

        try {
            SearchConditionVO vo = SearchConditionVO.builder()
                    .userInfoSeq(userSeq)
                    .menuCode(request.getMenuCode())
                    .conditionName(request.getConditionName())
                    // Map/List -> JSON String 변환
                    .searchOptions(objectMapper.writeValueAsString(request.getSearchOptions()))
                    .dateFilters(objectMapper.writeValueAsString(request.getDateFilters()))
                    .textFilters(objectMapper.writeValueAsString(request.getTextFilters()))
                    .useYn(request.getUseYn())
                    .createUser(userSeq)
                    .updateUser(userSeq)
                    .build();

            searchConditionMapper.insertCondition(vo);
            return vo.getConditionSeq();

        } catch (JsonProcessingException e) {

            throw new RuntimeException("저장 중 데이터 변환 오류가 발생했습니다.");
        }
    }

    /**
     * 2. 메뉴별 검색 조건 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public SearchConditionListResponse getSearchConditionList(String menuCode) {
        String userSeq = SecurityUtil.getUserInfoSeq();

        // 1. DB에서 VO 리스트 조회 (여기엔 searchOptions가 String으로 들어있음)
        List<SearchConditionVO> voList = searchConditionMapper.selectConditionList(userSeq, menuCode);

        // 2. [필수] VO를 Response DTO로 변환하여 String을 Map/List로 파싱
        List<SearchConditionResponse> dtoList = voList.stream()
                .map(vo -> SearchConditionResponse.of(vo, objectMapper)) // 여기서 파싱 발생!
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. 변환된 dtoList를 담아서 반환
        return SearchConditionListResponse.builder()
                .list(dtoList)
                .totalCount(dtoList.size())
                .build();
    }

    /**
     * 3. 검색 조건 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public SearchConditionResponse getSearchConditionDetail(String conditionSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();

        // 상세 조회 시에도 본인 확인을 위해 userSeq를 함께 전달하는 것이 안전합니다.
        SearchConditionVO vo = searchConditionMapper.selectConditionDetail(conditionSeq);

        // 정적 팩토리 메서드 사용
        return SearchConditionResponse.of(vo, objectMapper);
    }

    /**
     * 4. 검색 조건 삭제
     */
    @Override
    @Transactional
    public int deleteSearchCondition(String conditionSeq) {
        // 권한 확인을 위해 SecurityUtil에서 직접 꺼내어 전달
        return searchConditionMapper.deleteCondition(conditionSeq, SecurityUtil.getUserInfoSeq(), SecurityUtil.getOfficeSeq());
    }
}