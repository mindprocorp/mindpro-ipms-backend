package kr.co.mindpro.ipms.domain.history.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.history.login.service.LoginHistoryService;
import kr.co.mindpro.ipms.domain.history.login.dto.response.LoginHistoryResponse;
import kr.co.mindpro.ipms.domain.history.login.vo.LoginHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : seokho
 * @fileName : LoginHistoryController.java
 * @since : 2026. 4. 7.
 */
@RestController
@Tag(name = "로그인 이력 API", description = "로그인 이력 API")
@RequestMapping("/api/history/login")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    @Operation(summary = "로그인 이력 리스트 조회", description = "로그인 이력 테이블을 리스트 형태로 조회합니다.")
    @PostMapping("/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<LoginHistoryResponse>>> getLoginHistoryList(
            @RequestBody BaseSearchRequest searchReq
    ) {
        BaseSearchResponse<LoginHistoryResponse> res = loginHistoryService.getLoginHistoryList(searchReq);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로그인 이력 리스트 조회 성공", res));
    }

    @Operation(summary = "로그아웃 이력 저장", description = "로그아웃 이력을 저장합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<BaseSearchResponse<LoginHistoryVO>>> insertLogoutHistory(
            HttpServletRequest request
    ) {

        loginHistoryService.recordLogoutHistory(request);

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), "로그아웃 이력 저장 성공", null));
    }
}
