package kr.co.mindpro.ipms.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.FindIdRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ForgotPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.LoginRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ResetPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.TokenRefreshRequest;
import kr.co.mindpro.ipms.domain.auth.dto.response.FindIdResponse;
import kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse;


/**
 * AuthService Interface
 *
 * @author	 : mindpro
 * @fileName	 : AuthService.java
 * @since	 : 2025. 12. 24.
 */
public interface AuthService {

	/**
     * 로그인 실행 및 토큰 발급
     */
    LoginResponse login(LoginRequest request, HttpServletRequest httpServletRequest);

    /**
     * Refresh Token을 이용한 Access Token 재발급
     */
    LoginResponse refresh(TokenRefreshRequest request, HttpServletRequest httpServletRequest);

    /**
     * [멀티 사무소] active office 전환 + 새 JWT 발급.
     * 대상 사무소에 유저의 active membership이 있어야 하며, acct_status_code가 PENDING이면 거부.
     *
     * @param userId          로그인 ID (이메일)
     * @param userMstSeq      로그인 유저 PK
     * @param targetOfficeSeq 전환할 사무소 seq
     * @param isSuperAdmin    SUPER_ADMIN 여부 (true면 멤버십/PENDING 검증 우회)
     * @return 새 토큰이 담긴 LoginResponse
     */
    LoginResponse switchOffice(String userId, String userMstSeq, String targetOfficeSeq, boolean isSuperAdmin);

    /**
     * 아이디 찾기 (이름 + 휴대폰)
     */
    FindIdResponse findId(FindIdRequest request);

    /**
     * 비밀번호 찾기 (재설정 링크 이메일 발송)
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * 비밀번호 재설정 (토큰 검증 후 새 비밀번호 저장)
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * 아이디 중복 확인
     */
    boolean checkDuplicate(String userId);

    boolean checkPhoneDuplicate(String mobileNo);

    FindIdResponse findByNameAndPhoneForSocial(String userName, String mobileNo);
}