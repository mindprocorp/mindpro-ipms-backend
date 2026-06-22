package kr.co.mindpro.ipms.domain.conflict.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.conflict.dto.request.*;
import kr.co.mindpro.ipms.domain.conflict.dto.response.ConflictResponse;
import kr.co.mindpro.ipms.domain.conflict.service.ConflictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "이의심판 API", description = "이의심판 CRUD API")
@RestController
@RequestMapping("/api/conflict")
@RequiredArgsConstructor
public class ConflictController {

    private final ConflictService conflictService;

    @Operation(summary = "분쟁/심판 저장", description = "데이터(JSON)와 상표 이미지 파일들을 함께 등록 또는 수정합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictDetail>> createConflict(
            @RequestPart("data") ConflictRequest.ConflictDetail data,
            @RequestPart(value = "appTrademarkFile", required = false) MultipartFile appTrademarkFile,
            @RequestPart(value = "citedTrademarkFile", required = false) MultipartFile citedTrademarkFile) {
        ConflictResponse.ConflictDetail result = conflictService.createConflict(data, appTrademarkFile, citedTrademarkFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "저장 성공", result));
    }

    @Operation(summary = "분쟁/심판 상세 조회", description = "마스터 정보, 파일, 결과 리스트를 통합 조회합니다.")
    @GetMapping("/{conflictSeq}")
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictDetail>> getConflictDetail(@PathVariable String conflictSeq) {
        ConflictResponse.ConflictDetail detail = conflictService.getConflictDetail(conflictSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", detail));
    }

    @Operation(summary = "이의신청 리스트 검색 조회", description = "검색 조건(필터)에 따른 이의신청 리스트 조회를 한다.")
    @PostMapping("/list/search")
    public ResponseEntity<ApiResponse<BaseSearchResponse<ConflictResponse.ConflictListDetail>>> getConflictList(@RequestBody  BaseSearchRequest request) {
        BaseSearchResponse<ConflictResponse.ConflictListDetail> response = conflictService.getConflictList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "이의신청 검색 목록 조회 성공", response));
    }

    @Operation(summary = "기타사건 저장", description = "데이터(JSON)와 단일 이미지 파일을 함께 등록 또는 수정합니다.")
    @PostMapping(value = "/etc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictEtcCaseDetail>> createEtcCase(
            @RequestPart("data") ConflictRequest.ConflictEtcCaseDetail request,
            @RequestPart(value = "etcConflictFile", required = false) MultipartFile etcConflictFile) {
        ConflictResponse.ConflictEtcCaseDetail result = conflictService.createEtcConflict(request, etcConflictFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "기타사건 저장 성공", result));
    }

    @Operation(summary = "기타사건 상세 조회", description = "기타사건 전용 마스터 정보(국내등록번호 등)와 파일을 조회합니다.")
    @GetMapping("/etc/{conflictSeq}")
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictEtcCaseDetail>> getEtcConflictDetail(@PathVariable String conflictSeq) {
        ConflictResponse.ConflictEtcCaseDetail detail = conflictService.getEtcConflictDetail(conflictSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "기타사건 상세 조회 성공", detail));
    }

    @Operation(summary = "기타사건 리스트 검색 조회", description = "검색 조건에 따른 기타사건(국내등록번호 포함) 리스트를 조회합니다.")
    @PostMapping("/etc/list/search")
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictEtcList>> getEtcConflictList(@RequestBody  BaseSearchRequest request) {
        ConflictResponse.ConflictEtcList response = conflictService.getEtcConflictList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "기타사건 검색 목록 조회 성공", response));
    }

    @Operation(summary = "원심/하급심 결과 저장", description = "단건의 심판 결과 정보를 저장 또는 수정합니다.")
    @PostMapping("/result")
    public ResponseEntity<ApiResponse<Void>> saveConflictResult(@RequestBody ConflictRequest.ResultDetail request) {
        conflictService.createResultConflict(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "결과 정보가 저장되었습니다."));
    }

    @Operation(summary = "원심하급심 결과 목록 조회", description = "특정 사건의 원심하급심 결과 리스트를 조회합니다.")
    @PostMapping("/resultList")
    public ResponseEntity<ApiResponse<ConflictResponse.ConflictResultList>> getConflictResultList(@RequestParam String conflictSeq) {
        ConflictResponse.ConflictResultList response = conflictService.getConflictResultList(conflictSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "이의심판 결과 목록 조회 성공", response));
    }

    @Operation(summary = "분쟁/심판 삭제", description = "이의심판 또는 기타사건을 삭제합니다.")
    @DeleteMapping("/{conflictSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteConflict(@PathVariable String conflictSeq) {
        conflictService.deleteConflict(conflictSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공"));
    }

    @Operation(summary = "이미지 삭제", description = "이의심판 또는 기타사건에 등록된 이미지를 삭제합니다.")
    @DeleteMapping("/file/{conflictSeq}/{fileSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteConflictFile(
            @PathVariable String conflictSeq,
            @PathVariable String fileSeq) {
        conflictService.deleteConflictFile(conflictSeq, fileSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "이미지 삭제 성공"));
    }
}
