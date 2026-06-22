package kr.co.mindpro.ipms.domain.dispatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.dispatch.dto.request.DispatchRequest;
import kr.co.mindpro.ipms.domain.dispatch.dto.response.DispatchResponse;
import kr.co.mindpro.ipms.domain.dispatch.service.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [Controller] 문서수발 API
 */
@Tag(name = "문서수발 API", description = "우편물 등 문서 송수신 내역 관리 API")
@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchService dispatchService;

    @Operation(summary = "문서수발 목록 조회")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<DispatchResponse.DispatchDetail>>> getDispatchList(@RequestBody BaseSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "목록 조회 성공", dispatchService.getDispatchList(request)));
    }

    @Operation(summary = "문서수발 내역 저장")
    @PostMapping
    public ResponseEntity<ApiResponse<DispatchResponse.DispatchDetail>> saveDispatch(@Valid @RequestBody DispatchRequest.DispatchDetail request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공", dispatchService.saveDispatch(request)));
    }

    @Operation(summary = "문서수발 내역 삭제")
    @DeleteMapping("/{dispatchSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteDispatch(@PathVariable String dispatchSeq) {
        dispatchService.deleteDispatch(dispatchSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공"));
    }

    @Operation(summary = "문서수발 내역 일괄 삭제")
    @PostMapping("/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteDispatchList(@RequestBody List<String> ids) {
        dispatchService.deleteDispatchList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "일괄 삭제 성공"));
    }
}
