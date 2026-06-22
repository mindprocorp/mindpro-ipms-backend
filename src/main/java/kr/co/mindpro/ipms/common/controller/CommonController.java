package kr.co.mindpro.ipms.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.service.CommonService;
import kr.co.mindpro.ipms.domain.searchcondition.service.SearchConditionService;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "공통 API", description = "공통 API")
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;
    private final SearchConditionService searchConditionService; // 추가

    @Operation(summary = "유저정보 조회",description = "유저정보 조회한다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(@AuthenticationPrincipal UserDetails user) {

        UserResponse userResponse = commonService.getUserInfo(user);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "유저정보 조회 성공", userResponse));
    }



}
