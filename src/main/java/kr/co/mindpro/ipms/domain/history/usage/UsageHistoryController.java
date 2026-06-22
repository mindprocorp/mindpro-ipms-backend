package kr.co.mindpro.ipms.domain.history.usage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.usage.service.UsageHistoryService;
import kr.co.mindpro.ipms.domain.history.usage.dto.response.UsageHistoryResponse;
import kr.co.mindpro.ipms.domain.history.usage.vo.UsageHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : mindpro
 * @fileName : UsageHistoryController.java
 * @since : 2026. 4. 8.
 */
@Tag(name = "사용이력(Usage History)", description = "사용이력 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage-history")
public class UsageHistoryController {

    private final UsageHistoryService usageHistoryService;

    /**
     * 사용이력 리스트 검색 조회
     * */
    @Operation(summary = "리스트 검색 조회", description = "검색 조건에 맞는 사용이력 리스트를 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<UsageHistoryResponse>>> getUsageHistoryList(@RequestBody BaseSearchRequest request) {

        BaseSearchResponse<UsageHistoryResponse> response = usageHistoryService.getUsageHistoryList(request);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용이력 리스트 조회 성공", response));
    }
}
