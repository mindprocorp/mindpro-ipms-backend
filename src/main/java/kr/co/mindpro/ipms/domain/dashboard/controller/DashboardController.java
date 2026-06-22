package kr.co.mindpro.ipms.domain.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.dashboard.dto.response.DashboardResponse.DashboardOverview;
import kr.co.mindpro.ipms.domain.dashboard.service.DashboardService;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "대시보드 API", description = "대시보드 통계 API")
@RestController
@RequestMapping(value = "/api/dashboard", produces = "application/json; charset=UTF-8")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "대시보드 종합 통계 조회", description = "현재 사무소(JWT 의 office_seq) 범위로 종합 통계를 조회합니다.")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardOverview>> getDashboardSummary(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer year) {

        DashboardOverview res = dashboardService.getDashboardSummary(user.getOfficeSeq(), startDate, endDate, year);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "대시보드 통계 조회 성공", res));
    }
}
