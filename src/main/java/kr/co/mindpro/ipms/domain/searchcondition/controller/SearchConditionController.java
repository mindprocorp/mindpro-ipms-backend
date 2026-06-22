package kr.co.mindpro.ipms.domain.searchcondition.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.searchcondition.dto.request.SearchConditionRequest;
import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionListResponse;
import kr.co.mindpro.ipms.domain.searchcondition.dto.response.SearchConditionResponse;
import kr.co.mindpro.ipms.domain.searchcondition.service.SearchConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 검색 조건 관리 API
 *
 * @author   : min
 * @fileName : SearchConditionController.java
 * @since    : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "검색 조건 API", description = "검색 필터 저장 및 관리 API")
@RestController
@RequestMapping("/api/searchcondition")
@RequiredArgsConstructor
public class SearchConditionController {

    private final SearchConditionService searchConditionService;

    @Operation(summary = "검색 조건 저장", description = "현재 검색 필터를 저장합니다.")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<String>> saveSearchCondition(@RequestBody SearchConditionRequest request) {
        String conditionSeq = searchConditionService.saveSearchCondition(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공", conditionSeq));
    }

    @Operation(summary = "검색 조건 목록 조회", description = "메뉴별 저장된 검색 조건 목록을 조회합니다.")
    @GetMapping("/list/{menuCode}")
    public ResponseEntity<ApiResponse<SearchConditionListResponse>> getSearchConditionList(
            @Parameter(description = "메뉴 코드 (예: CONFLICT, APP)") @PathVariable String menuCode) {

        SearchConditionListResponse response = searchConditionService.getSearchConditionList(menuCode);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "목록 조회 성공", response));
    }

    @Operation(summary = "검색 조건 상세 조회", description = "특정 검색 조건의 상세 필터 데이터를 조회합니다.")
    @GetMapping("/detail/{conditionSeq}")
    public ResponseEntity<ApiResponse<SearchConditionResponse>> getSearchConditionDetail(
            @Parameter(description = "검색 조건 시퀀스") @PathVariable String conditionSeq) {

        SearchConditionResponse response = searchConditionService.getSearchConditionDetail(conditionSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "상세 조회 성공", response));
    }

    @Operation(summary = "검색 조건 삭제", description = "저장된 검색 조건을 삭제(논리 삭제)합니다.")
    @DeleteMapping("/{conditionSeq}")
    public ResponseEntity<ApiResponse<Integer>> deleteSearchCondition(
            @Parameter(description = "검색 조건 시퀀스") @PathVariable String conditionSeq) {

        int result = searchConditionService.deleteSearchCondition(conditionSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공", result));
    }
}