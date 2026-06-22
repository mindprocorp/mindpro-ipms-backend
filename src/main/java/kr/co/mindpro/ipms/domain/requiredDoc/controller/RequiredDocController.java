package kr.co.mindpro.ipms.domain.requiredDoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.request.RequiredDocRequest;
import kr.co.mindpro.ipms.domain.requiredDoc.dto.response.RequiredDocResponse;
import kr.co.mindpro.ipms.domain.requiredDoc.service.RequiredDocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : seokho
 * @fileName : RequiredDocController.java
 * @since : 2026. 4. 1.
 */
@Slf4j
@Tag(name = "구비서류 탭 API", description = "구비서류 탭 관련 CRUD API")
@RestController
@RequestMapping("/api/required-doc")
@RequiredArgsConstructor
public class RequiredDocController {

    private final RequiredDocService requiredDocService;

    @Operation(summary = "구비서류 단건 등록", description = "구비서류 단건을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createRequiredDoc(@RequestBody RequiredDocRequest.createRequiredDocRequest request) {

        requiredDocService.createRequiredDoc(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "구비서류 탭 단건 등록이 완료되었습니다."));
    }

    @Operation(summary = "구비서류 목록 조회", description = "구비서류 목록을 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<RequiredDocResponse.RequiredDocListResponse>>> getRequiredDocListByAppSeq(@RequestBody BaseSearchRequest request) {

        BaseSearchResponse<RequiredDocResponse.RequiredDocListResponse> response = requiredDocService.getRequiredDocListByAppSeq(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 목록 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "구비서류 단건 조회", description = "구비서류 단건을 조회합니다.")
    @GetMapping("/{appSeq}/{requiredDocSeq}")
    public ResponseEntity<ApiResponse<RequiredDocResponse.RequiredDocDetailResponse>> getRequiredDocDetail(@PathVariable String appSeq, @PathVariable String requiredDocSeq) {

        RequiredDocResponse.RequiredDocDetailResponse response = requiredDocService.getRequiredDocDetail(appSeq, requiredDocSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 단건 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "구비서류 단건 논리적 삭제", description = "구비서류 단건을 논리적으로 삭제합니다.")
    @DeleteMapping("/delete/soft/{appSeq}/{requiredDocSeq}")
    public ResponseEntity<ApiResponse<Void>> softDeleteRequiredDoc(@PathVariable String appSeq, @PathVariable String requiredDocSeq) {

        requiredDocService.softDeleteRequiredDoc(appSeq, requiredDocSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 단건 논리적 삭제가 완료되었습니다."));
    }

    @Operation(summary = "구비서류 다건 논리 삭제", description = "구비서류 다건을 논리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/soft/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiSoftDeleteRequiredDoc(
            @PathVariable String appSeq,
            @RequestBody List<String> requiredDocSeqList) {

        requiredDocService.softDeleteRequiredDocByList(appSeq, requiredDocSeqList);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 다건 논리 삭제가 완료되었습니다."));
    }

    @Operation(summary = "구비서류 단건 물리 삭제", description = "구비서류 단건을 물리적으로 삭제합니다.")
    @DeleteMapping("/delete/hard/{appSeq}/{requiredDocSeq}")
    public ResponseEntity<ApiResponse<Void>> hardDeleteRequiredDoc(
            @PathVariable String appSeq,
            @PathVariable String requiredDocSeq) {

        requiredDocService.hardDeleteRequiredDoc(appSeq, requiredDocSeq);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 단건 물리 삭제가 완료되었습니다."));
    }

    @Operation(summary = "구비서류 다건 물리 삭제", description = "구비서류 다건을 물리적으로 삭제합니다.")
    @DeleteMapping("/multi-delete/hard/{appSeq}")
    public ResponseEntity<ApiResponse<Void>> multiHardDeleteRequiredDoc(
            @PathVariable String appSeq,
            @RequestBody List<String> requiredDocSeqList) {

        requiredDocService.hardDeleteRequiredDocByList(appSeq, requiredDocSeqList);

        return ResponseEntity.ok()
                .body(ApiResponse.success(HttpStatus.OK.value(), "구비서류 다건 물리 삭제가 완료되었습니다."));
    }
}
