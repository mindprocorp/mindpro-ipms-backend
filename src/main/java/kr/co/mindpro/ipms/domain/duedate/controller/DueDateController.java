package kr.co.mindpro.ipms.domain.duedate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.duedate.dto.request.DueDateSaveRequest;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateDetailResponse;
import kr.co.mindpro.ipms.domain.duedate.dto.response.DueDateResponse;
import kr.co.mindpro.ipms.domain.duedate.service.DueDateService;
import kr.co.mindpro.ipms.domain.duedate.vo.DueDateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : DuedateService.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "기일 API", description = "기일 CRUD API")
@RestController
@RequestMapping("/api/duedate")
@RequiredArgsConstructor
public class DueDateController {

    private final DueDateService duedateService;

//    @Operation(summary = "기일 일괄 저장")
//    @PostMapping("/save")
//    public ResponseEntity<ApiResponse<Void>> saveAllDueDates(@Valid @RequestBody DueDateSaveRequest request) {
//        duedateService.saveAllDueDates(request);
//        // [수정] 데이터가 없으므로 (상태코드, 메시지) 형태 사용
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success(HttpStatus.CREATED.value(), "기일 정보 저장 및 수정이 완료되었습니다."));
//    }
//
//    @Operation(summary = "업무별 기일 상세 조회")
//    @GetMapping("/detail/{officeSeq}/{tblSeq}")
//    public ResponseEntity<ApiResponse<DueDateDetailResponse>> getDueDateDetails(
//            @PathVariable String officeSeq, @PathVariable String tblSeq) {
//
//        DueDateDetailResponse response = duedateService.getDueDateMapByWork(tblSeq, officeSeq);
//        // [수정] 데이터가 있으므로 (상태코드, 메시지, 데이터) 형태 사용
//        return ResponseEntity.ok(
//                ApiResponse.success(HttpStatus.OK.value(), "기일 상세조회 성공", response)
//        );
//    }

    @Operation(summary = "기일 통합 검색")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<DueDateResponse.DueDateDetail>>> getDueDateList(@RequestBody BaseSearchRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "기일 리스트 조회 성공", duedateService.getDueDateList(request))
        );
    }

    @Operation(summary = "다가오는 기일 조회")
    @PostMapping("/upcoming/{officeSeq}")
    public ResponseEntity<ApiResponse<List<DueDateVO>>> getUpcomingDueDates(@PathVariable String officeSeq) {
        List<DueDateVO> list = duedateService.getUpcomingDueDates(officeSeq);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "다가오는 기일 조회 성공", list)
        );
    }

    @Operation(summary = "기일 단건 등록")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerDueDate(@Valid @RequestBody DueDateVO vo) {
        duedateService.registerDueDate(vo);
        // [수정] 데이터가 없으므로 (상태코드, 메시지) 형태 사용
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "기일 단건 등록이 완료되었습니다."));
    }

    @Operation(summary = "기일 완료 처리 (Y/N 토글)")
    @PatchMapping("/{duedateSeq}/complete")
    public ResponseEntity<ApiResponse<Void>> updateCompleteYn(
            @PathVariable String duedateSeq,
            @RequestParam String completeYn,
            @RequestParam String dueTypeCategoryCode) {
        duedateService.updateCompleteYn(duedateSeq, completeYn, dueTypeCategoryCode);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "완료 상태 변경 성공"));
    }

    @Operation(summary = "접발송 내역 목록 조회")
    @PostMapping("/progress-history/list")
    public ApiResponse<BaseSearchResponse<DueDateResponse.ProgressHistoryDetail>> getProgressHistoryList(
            @Valid @RequestBody BaseSearchRequest request) {

        // 2. 서비스 호출
        BaseSearchResponse<DueDateResponse.ProgressHistoryDetail> response = duedateService.getProgressHistoryList(request);

        // 3. ApiResponse 형식에 맞춰 반환
        return ApiResponse.success(HttpStatus.OK.value(), "접발송목록 조회 성공", response);
    }
}

