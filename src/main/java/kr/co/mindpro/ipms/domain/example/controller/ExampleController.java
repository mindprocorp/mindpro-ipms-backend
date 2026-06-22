package kr.co.mindpro.ipms.domain.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleCreateRequest;
import kr.co.mindpro.ipms.domain.example.dto.request.ExampleListRequest;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleDetailResponse;
import kr.co.mindpro.ipms.domain.example.dto.response.ExampleListResponse;
import kr.co.mindpro.ipms.domain.example.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * [Controller] 이의심판 API
 *
 * @author	 : min
 * @fileName : ConflictController.java
 * @since	 : 2026. 01. 07.
 */
@Slf4j
@Tag(name = "이의심판 API", description = "이의심판 CRUD API")
@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService conflictService;

//
//    /**
//     * 이의심판 등록 API
//     * POST /api/
//     */
//    @Operation(summary = "이의심판 등록", description = "이의심판 등록 API")
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> createAndModifyConflict(@Valid @RequestBody ExampleCreateRequest request) {
//        conflictService.createAndModifyConflict(request);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.success(HttpStatus.CREATED.value(), "이의심판 등록이 완료되었습니다."));
//    }
//
//    /**
//     * 이의신청 상세 조회 API
//     * GET /api/conflict
//     */
//    @Operation(summary = "이의신청 상세 조회", description = "이의신청 상세 조회를 한다. ")
//    @GetMapping("/{confictSeq}")
//    public ResponseEntity<ApiResponse<ExampleDetailResponse>> getConflictDetail(@PathVariable String confictSeq ) {
//        ExampleDetailResponse oppoDetailResponse = conflictService.getConflictDetail(confictSeq);
//        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "전체 사용자 목록 조회 성공", oppoDetailResponse));
//    }
//
//    /**
//     * 이의신청 리스트 조회 API
//     * GET /api/conflict
//     */
//    @Operation(summary = "이의신청 리스트 조회", description = "이의신청 리스트 조회를 한다. ")
//    @PostMapping("/list")
//    public ResponseEntity<ApiResponse<ExampleListResponse>> getConflictList(@RequestBody ExampleListRequest conflictListRequest) {
//        ExampleListResponse conflictListResponse = conflictService.getConflictList(conflictListRequest);
//        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "전체 이의신청 목록 조회 성공", conflictListResponse));
//    }
//
//    /**
//     * test 조회 API
//     * GET /api/conflict
//     */
//    @Operation(summary = "test 상세 조회", description = "test 상세 조회를 한다. ")
//    @GetMapping("")
//    public ResponseEntity<ApiResponse<String>> getTest() {
//        String response = conflictService.getTest();
//        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "전체 사용자 목록 조회 성공", response));
//    }
//


}