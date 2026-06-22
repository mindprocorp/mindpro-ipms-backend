package kr.co.mindpro.ipms.common.nts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.nts.dto.NtsDto;
import lombok.RequiredArgsConstructor;

/**
 * [Controller] 국세청 사업자 정보 조회 API
 *
 * @author   : mindpro
 * @since    : 2026. 1. 12.
 */
@Tag(name = "NTS Support", description = "국세청 연동 지원 API")
@RestController
@RequestMapping("/api/v1/common/registry/public/nts")
@RequiredArgsConstructor
public class NtsController {

    private final NtsApiService ntsApiService;

    @Operation(summary = "사업자등록 상태 실시간 조회 (국세청)")
    @GetMapping("/status")
    public ApiResponse<NtsDto.StatusResponse> checkStatus(
    		@Parameter(description = "사업자번호", example = "1328636925")
    		@RequestParam String bno) {
        // 기존 processRegistry 패턴처럼 Service에 위임하여ApiResponse 반환
        return ntsApiService.getBusinessStatus(bno);
    }
}
