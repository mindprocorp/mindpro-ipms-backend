package kr.co.mindpro.ipms.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.common.exception.ErrorResponse;
import kr.co.mindpro.ipms.domain.auth.dto.request.FindIdRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ForgotPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.LoginRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ResetPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.TokenRefreshRequest;
import kr.co.mindpro.ipms.domain.auth.dto.response.FindIdResponse;
import kr.co.mindpro.ipms.domain.auth.dto.request.SocialLoginRequest;
import kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse;
import kr.co.mindpro.ipms.domain.auth.dto.response.SocialLoginResponse;
import kr.co.mindpro.ipms.domain.auth.service.AuthService;
import kr.co.mindpro.ipms.domain.auth.service.SocialAuthService;
import kr.co.mindpro.ipms.common.notification.EmailService;
import kr.co.mindpro.ipms.common.notification.VerificationCodeService;
import lombok.RequiredArgsConstructor;
    
/**
 * 인증 관련 API
 * 모든 인증 요청은 /api/auth 경로로 진입.
 *
 * @author	 : mindpro
 * @fileName	 : AuthController.java
 * @since	 : 2025. 12. 24.
 */
@Tag(name = "Auth (인증)", description = "로그인 및 토큰 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SocialAuthService socialAuthService;
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;

    @Operation(summary = "로그인 실행", description = "ID/PW를 검증하고 Access/Refresh 토큰을 발급합니다.")
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공",
    				content = @Content(schema = @Schema(implementation = ApiResponse.class, subTypes = { LoginResponse.class }))),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패 (아이디/비밀번호 불일치)", 
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", 
                     content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        // 서비스에서 토큰 생성
        LoginResponse loginResponse = authService.login(request, httpServletRequest);
        
        return withAuthCookies(loginResponse.accessToken(), loginResponse.refreshToken(),
                "로그인이 완료되었습니다.", loginResponse);
    }
    
    /**
     * 토큰 재발급 API
     * [변경] JSON Body 대신 쿠키에서 직접 읽어오며 Swagger에도 이를 명시합니다.
     */
    @Operation(
        summary = "토큰 재발급", 
        description = "쿠키에 담긴 Refresh Token을 자동으로 읽어 새로운 토큰 세트를 발급받습니다."
    )
    @ApiResponses(value = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
    				content = @Content(schema = @Schema(implementation = ApiResponse.class, subTypes = { LoginResponse.class }))),
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
    				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
        @Parameter(
            name = "refreshToken", 
            description = "로그인 시 발급된 HttpOnly 리프레시 토큰", 
            in = ParameterIn.COOKIE, // Swagger UI에서 쿠키 위치로 표시
            required = true,
            schema = @Schema(type = "string")
        )
        @CookieValue(name = "refreshToken") String refreshToken, // 쿠키에서 직접 주입
        HttpServletRequest request, HttpServletResponse response
    ) {
        // [참고] TokenRefreshRequest DTO 대신 문자열을 직접 서비스에 전달하거나 
        // 서비스 인터페이스를 수정하지 않으려면 new TokenRefreshRequest(refreshToken)으로 래핑합니다.
        LoginResponse loginResponse = authService.refresh(new TokenRefreshRequest(refreshToken), request);
        return withAuthCookies(loginResponse.accessToken(), loginResponse.refreshToken(),
                "토큰이 재발급되었습니다.", loginResponse);
    }

    @Operation(summary = "아이디 찾기", description = "이름과 휴대폰 번호로 가입된 아이디를 찾습니다.")
    @PostMapping("/find-id")
    public ResponseEntity<ApiResponse<FindIdResponse>> findId(@Valid @RequestBody FindIdRequest request) {
        FindIdResponse response = authService.findId(request);
        return ResponseEntity.ok(ApiResponse.success(200, "아이디 찾기 결과입니다.", response));
    }

    @Operation(summary = "비밀번호 찾기", description = "본인 확인 후 비밀번호 재설정 링크를 이메일로 발송합니다.")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(200, "비밀번호 재설정 링크가 이메일로 발송되었습니다.", null));
    }

    @Operation(summary = "비밀번호 재설정", description = "이메일 링크의 토큰을 검증하고 새 비밀번호를 설정합니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(200, "비밀번호가 변경되었습니다.", null));
    }

    @Operation(summary = "이메일 인증 코드 발송", description = "회원가입 시 이메일 인증 코드를 발송합니다.")
    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<Void>> sendVerification(@RequestParam String email) {
        String code = verificationCodeService.generateCode(email);
        emailService.sendVerificationEmail(email, code);
        return ResponseEntity.ok(ApiResponse.success(200, "인증 코드가 이메일로 발송되었습니다.", null));
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "이메일 인증 코드를 검증합니다.")
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Boolean>> verifyEmail(
            @RequestParam String email, @RequestParam String code) {
        boolean verified = verificationCodeService.verify(email, code);
        String message = verified ? "이메일 인증이 완료되었습니다." : "인증 코드가 올바르지 않거나 만료되었습니다.";
        return ResponseEntity.ok(ApiResponse.success(200, message, verified));
    }

    @Operation(summary = "아이디 중복 확인", description = "회원가입 시 아이디(이메일) 중복 여부를 확인합니다.")
    @GetMapping("/check-duplicate")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicate(@RequestParam String userId) {
        boolean exists = authService.checkDuplicate(userId);
        String message = exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.";
        return ResponseEntity.ok(ApiResponse.success(200, message, exists));
    }

    @Operation(summary = "전화번호 중복 확인", description = "회원가입 시 전화번호 중복 여부를 확인합니다.")
    @GetMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(@RequestParam String mobileNo) {
        boolean exists = authService.checkPhoneDuplicate(mobileNo);
        String message = exists ? "이미 가입된 전화번호입니다. 기존 아이디로 로그인해주세요." : "";
        return ResponseEntity.ok(ApiResponse.success(200, message, exists));
    }

    @Operation(summary = "소셜 기존 계정 확인 (연동 전 조회만)",
               description = "이름+전화번호로 기존 계정 존재 여부를 확인합니다.")
    @PostMapping("/social/check")
    public ResponseEntity<ApiResponse<FindIdResponse>> socialCheck(
            @RequestParam String userName,
            @RequestParam String mobileNo) {
        FindIdResponse result = authService.findByNameAndPhoneForSocial(userName, mobileNo);
        if (result != null) {
            return ResponseEntity.ok(ApiResponse.success(200, "기존 계정이 확인되었습니다.", result));
        }
        return ResponseEntity.ok(ApiResponse.success(200, "일치하는 계정이 없습니다.", null));
    }

    @Operation(summary = "소셜 기존 계정 연동",
               description = "확인된 기존 계정에 소셜 로그인을 연동 후 로그인합니다.")
    @PostMapping("/social/link")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> socialLink(
            @RequestParam String userName,
            @RequestParam String mobileNo,
            @RequestParam String provider,
            @RequestParam String providerId,
            @RequestParam(required = false) String socialEmail) {
        SocialLoginResponse result = socialAuthService.linkExistingAccount(
                userName, mobileNo, provider, providerId, socialEmail);

        if (result.authenticated() && result.loginResponse() != null) {
            return withAuthCookies(result.loginResponse().accessToken(), result.loginResponse().refreshToken(),
                    "기존 계정에 소셜 로그인이 연동되었습니다.", result);
        }

        return ResponseEntity.ok(ApiResponse.success(200, "일치하는 계정이 없습니다.", result));
    }

    @Operation(
        summary = "소셜 로그인",
        description = "소셜 인가 코드로 로그인합니다. 기존 회원이면 JWT를 발급하고, 신규 회원이면 회원가입 정보를 반환합니다."
    )
    @PostMapping("/social/{provider}")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> socialLogin(
        @Parameter(description = "소셜 제공자 (kakao, naver, google)")
        @org.springframework.web.bind.annotation.PathVariable String provider,
        @Valid @RequestBody SocialLoginRequest request
    ) {
        SocialLoginResponse result = socialAuthService.socialLogin(
            provider.toUpperCase(), request.code(), request.redirectUri()
        );

        if (result.authenticated() && result.loginResponse() != null) {
            return withAuthCookies(result.loginResponse().accessToken(), result.loginResponse().refreshToken(),
                    "소셜 로그인이 완료되었습니다.", result);
        }

        return ResponseEntity.ok(ApiResponse.success(200, "회원가입이 필요합니다.", result));
    }

    private <T> ResponseEntity<ApiResponse<T>> withAuthCookies(String accessToken, String refreshToken,
                                                                String message, T data) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true).secure(true).path("/").maxAge(1800).sameSite("Lax").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(604800).sameSite("Lax").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success(200, message, data));
    }
}