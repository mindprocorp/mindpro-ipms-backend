package kr.co.mindpro.ipms.domain.history.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.dto.request.HistoryRequest;
import kr.co.mindpro.ipms.domain.history.dto.response.HistoryResponse;
import kr.co.mindpro.ipms.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "히스토리 API", description = "전체 업무 이력 조회 API")
@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @Operation(summary = "히스토리 목록 검색")
    @PostMapping("/list/search")
    public ResponseEntity<ApiResponse<BaseSearchResponse<HistoryResponse.HistorySearchListDetail>>> getHistoryList(
            @Valid @RequestBody BaseSearchRequest request) {
        
        BaseSearchResponse<HistoryResponse.HistorySearchListDetail> response = historyService.getHistoryList(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "히스토리 목록 조회 성공", response));
    }

    @Operation(summary = "변경 이력 상세 조회")
    @GetMapping("/detail/{modifiedHistSeq}")
    public ResponseEntity<ApiResponse<HistoryResponse.ModifiedHistDetail>> getHistoryDetail(@PathVariable String modifiedHistSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "변경 이력 상세 조회 성공", historyService.getHistoryDetail(modifiedHistSeq)));
    }

    @Operation(summary = "변경 사항 기록")
    @PostMapping("/save") // /save 중복 방지
    public ResponseEntity<ApiResponse<HistoryResponse.ModifiedHistDetail>> saveHistory(@RequestBody HistoryRequest.ModifiedHistDetail request) {

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "변경 사항이 기록되었습니다.", historyService.saveHistory(request)));
    }
    @Operation(summary = "변경 이력 삭제")
    @DeleteMapping("/{modifiedHistSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteModifiedHist(@PathVariable String modifiedHistSeq) {
        historyService.deleteModifiedHist(modifiedHistSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "변경 이력 삭제 성공"));
    }

    @Operation(summary = "변경 이력 일괄 삭제")
    @PostMapping("/delete-list")
    public ResponseEntity<ApiResponse<Void>> deleteModifiedHistList(@RequestBody java.util.List<String> ids) {
        historyService.deleteModifiedHistList(ids);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "변경 이력 리스트 삭제 성공"));
    }
}
