package kr.co.mindpro.ipms.domain.auth.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.mindpro.ipms.common.util.ClientIpUtil;
import kr.co.mindpro.ipms.domain.history.login.service.impl.LoginHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.notification.EmailService;
import kr.co.mindpro.ipms.domain.auth.dto.request.FindIdRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ForgotPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.LoginRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.ResetPasswordRequest;
import kr.co.mindpro.ipms.domain.auth.dto.request.TokenRefreshRequest;
import kr.co.mindpro.ipms.domain.auth.dto.response.FindIdResponse;
import kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse;
import kr.co.mindpro.ipms.domain.auth.service.AuthService;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final LoginHistoryServiceImpl loginHistoryService;

    @Value("${jwt.expiration}")
    private long accessTokenValidityInMilliseconds;

    @Value("${app.front-url:http://localhost:3000}")
    private String frontUrl;

    private static final int MAX_LOGIN_FAIL_COUNT = 5;

    @Override
    @Transactional(noRollbackFor = BusinessException.class)
    public LoginResponse login(LoginRequest request, HttpServletRequest httpServletRequest) {
        // 추가 26. 4. 7.
        String clientIp = ClientIpUtil.getClientIp(httpServletRequest);
        String userAgent = httpServletRequest.getHeader("User-Agent");

        // 1. 사용자 존재 여부 확인
//        UserMasterVO user = userMapper.findByUserId(request.userId())
//                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_FAILED));

        // 1. 사용자 존재 여부 확인 -> 수정
        Optional<UserMasterVO> userOpt = userMapper.findByUserId(request.userId());
        if (userOpt.isEmpty()) {
            // [이력 추가] 존재하지 않는 계정 시도
            loginHistoryService.recordLoginHistory("unKnown", request.userId(), clientIp, "LOGIN", userAgent, "N", "NORMAL", "존재하지 않는 아이디 시도");
            throw new BusinessException(ErrorCode.AUTH_FAILED);
        }

        UserMasterVO user = userOpt.get();
        String userMstSeq = user.getUserMstSeq();
        String officeSeq = user.getOfficeSeq();

        // 2. 계정 삭제 여부
        if ("Y".equals(user.getDelYn())) {
            // [이력 추가] 삭제된 계정
            loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGIN", userAgent, "N", "NORMAL", "삭제된 계정 접속 시도");
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 계정 잠금 여부
        if ("Y".equals(user.getLoginLockYn())) {
            // [이력 추가] 잠긴 계정
            loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp,"LOGIN", userAgent, "N", "NORMAL", "잠긴 계정 접속 시도");
            throw new BusinessException("계정이 잠겼습니다. 관리자에게 문의하세요.", ErrorCode.AUTH_FAILED);
        }

        // 3-1. 사무소 승인 대기(PENDING) 상태면 로그인 거부
        if ("PENDING".equals(user.getAcctStatusCode())) {
            loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGIN", userAgent, "N", "NORMAL", "승인 대기 계정 접속 시도");
            throw new BusinessException("관리자 승인 대기 중입니다. 승인 완료 후 로그인 가능합니다.", ErrorCode.AUTH_FAILED);
        }

        // 4. 비밀번호 검증
        if (!passwordEncoder.matches(request.userPw(), user.getUserPassword())) {
            userMapper.incrementLoginFailCount(request.userId());

            int failCount = Integer.parseInt(
                    user.getLoginFailCount() != null ? user.getLoginFailCount() : "0") + 1;

            if (failCount >= MAX_LOGIN_FAIL_COUNT) {
                userMapper.lockAccount(request.userId());
                // [이력 추가] 비밀번호 오류 초과로 인한 잠금
                loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGIN", userAgent, "N", "NORMAL", "비밀번호 " + MAX_LOGIN_FAIL_COUNT + "회 오류 (계정 잠금 처리됨)");
                throw new BusinessException(
                        "비밀번호 " + MAX_LOGIN_FAIL_COUNT + "회 오류로 계정이 잠겼습니다.", ErrorCode.AUTH_FAILED);
            }

            // [이력 추가] 단순 비밀번호 불일치
            loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGIN", userAgent, "N", "NORMAL", "비밀번호 불일치 (" + failCount + "/" + MAX_LOGIN_FAIL_COUNT + ")");
            throw new BusinessException(
                    "비밀번호가 일치하지 않습니다. (" + failCount + "/" + MAX_LOGIN_FAIL_COUNT + ")", ErrorCode.AUTH_FAILED);
        }

        // 5. 로그인 성공: 실패 횟수 초기화
        userMapper.resetLoginFailCount(request.userId());

        // 6. [LEGACY] JWT auth claim 발급용 — utb_user_role에서 role 문자열 가져옴.
        //    실제 권한 분기는 oe.role_seq + admin_auth 기반 (CommonService.getUserInfo 참조).
        //    utb_user_role 제거 시 oe.role_seq → role_type 기반으로 통합 예정.
        List<String> userRoles = userMapper.getUserRoleByMstSeq(user.getUserMstSeq());
        String rolePriority = userRoles.isEmpty() ? "ROLE_GUEST" : userRoles.get(0);

        // 7. 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(), user.getOfficeSeq(), rolePriority);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(), user.getOfficeSeq(), rolePriority);

        // [이력 추가] 최종 로그인 성공 기록
        loginHistoryService.recordLoginHistory(officeSeq, userMstSeq, clientIp, "LOGIN", userAgent, "Y", "NORMAL", "정상 로그인");

        // 8. 응답
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidityInMilliseconds / 1000)
                .userInfoSeq(user.getUserInfoSeq())
                .userMstSeq(user.getUserMstSeq())
                .officeSeq(user.getOfficeSeq())
                .userRole(rolePriority)
                .build();
    }

    /**
     * [멀티 사무소] active office 전환 + JWT 재발급.
     *  - 대상 사무소에 active office_employee 가 있어야 함
     *  - PENDING 상태인 membership은 거부
     *  - user_info.office_seq / login_info.office_seq 동기화 후 새 토큰 발급
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse switchOffice(String userId, String userMstSeq, String targetOfficeSeq, boolean isSuperAdmin) {
        if (userId == null || userId.isBlank()
                || userMstSeq == null || userMstSeq.isBlank()
                || targetOfficeSeq == null || targetOfficeSeq.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 1. 대상 사무소에 active membership 존재 확인 (슈퍼관리자는 우회 — utb_office_employee INSERT 하지 않음)
        if (!isSuperAdmin && userMapper.existsActiveMembership(userMstSeq, targetOfficeSeq) <= 0) {
            throw new BusinessException("해당 사무소에 소속되어 있지 않습니다.", ErrorCode.ACCESS_DENIED);
        }

        // 2. office_seq 동기 갱신 (user_info + login_info)
        userMapper.switchActiveOffice(userMstSeq, targetOfficeSeq);
        userMapper.switchActiveOfficeLogin(userMstSeq, targetOfficeSeq);

        // 3. 전환 후 최신 상태 재조회 (새 login.office_seq 기준 findByUserId — E join도 새 사무소로 붙음)
        UserMasterVO user = userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 4. PENDING 차단 (슈퍼관리자는 멤버십이 없으므로 acct_status가 비거나 PENDING으로 잡힐 수 있어 우회)
        if (!isSuperAdmin && "PENDING".equals(user.getAcctStatusCode())) {
            throw new BusinessException("해당 사무소는 관리자 승인 대기 중입니다.", ErrorCode.AUTH_FAILED);
        }

        // 5. [LEGACY] JWT auth claim 발급용 — utb_user_role 기반. (전환 후 새 토큰)
        //    실제 권한은 oe.role_seq + admin_auth 기반. utb_user_role 제거 시 함께 정리.
        //
        //    [슈퍼관리자 전환 정책] "그 사무소의 본인 권한 그대로"
        //    - 본거지(본인 SUPER_ADMIN 매핑): ROLE_SUPER_ADMIN
        //    - 그 외 본인 멤버 사무소: 그 사무소의 멤버십 role_type 기반
        //    - 비멤버 사무소: ROLE_SYSTEM_ADMIN (그 회사 시스템관리자처럼 진입)
        String rolePriority;
        if (isSuperAdmin) {
            String targetRoleType = userMapper.findRoleTypeByMembership(user.getUserMstSeq(), targetOfficeSeq);
            if (targetRoleType != null && !targetRoleType.isBlank()) {
                rolePriority = "ROLE_" + targetRoleType;  // SUPER_ADMIN/SYSTEM_ADMIN/SYSTEM_USER/CUSTOM
            } else {
                // 비멤버 사무소: 가상 시스템관리자로 진입
                rolePriority = "ROLE_SYSTEM_ADMIN";
            }
        } else {
            List<String> userRoles = userMapper.getUserRoleByMstSeq(user.getUserMstSeq());
            rolePriority = userRoles.isEmpty() ? "ROLE_GUEST" : userRoles.get(0);
        }

        String accessToken  = jwtTokenProvider.createToken(user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(), targetOfficeSeq, rolePriority);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(), targetOfficeSeq, rolePriority);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidityInMilliseconds / 1000)
                .userInfoSeq(user.getUserInfoSeq())
                .userMstSeq(user.getUserMstSeq())
                .officeSeq(targetOfficeSeq)
                .userRole(rolePriority)
                .build();
    }

    /**
     * Refresh Token을 이용한 토큰 재발급 로직
     */
    @Override
    @Transactional(readOnly = true)
    public LoginResponse refresh(TokenRefreshRequest request, HttpServletRequest httpServletRequest) {
        String refreshToken = request.refreshToken();

        // 1. Refresh Token 유효성 검증
        jwtTokenProvider.validateToken(refreshToken);

        // 2. 토큰에서 정보 추출
        String userId = jwtTokenProvider.getUsername(refreshToken);
        String userInfoSeq = jwtTokenProvider.getUserInfoSeq(refreshToken);
        String userMstSeq = jwtTokenProvider.getUserMstSeq(refreshToken);
        String officeSeq = jwtTokenProvider.getOfficeSeq(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        // 3. 새로운 Access Token 및 Refresh Token 생성 (Refresh Token Rotation)
        String newAccessToken = jwtTokenProvider.createToken(userId, userInfoSeq, userMstSeq, officeSeq, role);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId, userInfoSeq, userMstSeq, officeSeq, role);

        // SecurityContext에 새 인증 주입 — 후속 AOP(UsageHistoryAspect)가 office_seq/userMstSeq를 읽을 수 있게 함
        Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* 토큰 재발급 이력 남기는 로직 일단 주석 처리
        try {
            String clientIp = ClientIpUtil.getClientIp(httpServletRequest);
            String userAgent = httpServletRequest.getHeader("User-Agent");

            // 로그인 타입을 "REFRESH"로 줘서 일반 로그인("NORMAL")과 확실히 구분
            loginHistoryService.recordLoginHistory(
                    userMstSeq,
                    clientIp,
                    userAgent,
                    "Y",
                    "REFRESH",
                    "토큰 재발급"
            );
        } catch (Exception e) {
            log.error(">>> [ERROR] 토큰 재발급 이력 저장 실패: {}", e.getMessage());
        }
        */

        // 4. 응답 조립
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidityInMilliseconds / 1000)
                .userInfoSeq(userInfoSeq)
                .userMstSeq(userMstSeq)
                .officeSeq(officeSeq)
                .userRole(role)
                .build();
    }

    /** 아이디 찾기: 이름 + 휴대폰으로 마스킹된 아이디 반환 */
    @Override
    @Transactional(readOnly = true)
    public FindIdResponse findId(FindIdRequest request) {
        UserMasterVO user = userMapper.findByNameAndPhone(request.userNameKo(), request.userMobileNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String maskedId = maskEmail(user.getUserId());
        String registeredDate = user.getCreateAt() != null
                ? user.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "";

        return FindIdResponse.builder()
                .maskedUserId(maskedId)
                .registeredDate(registeredDate)
                .build();
    }

    /** 비밀번호 찾기: 본인 확인 후 재설정 링크를 이메일로 발송 */
    @Override
    @Transactional(readOnly = true)
    public void forgotPassword(ForgotPasswordRequest request) {
        UserMasterVO user = userMapper.findByUserIdAndNameAndPhone(
                request.userId(), request.userNameKo(), request.userMobileNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // JWT 리셋 토큰 생성 (15분 만료)
        String resetToken = jwtTokenProvider.createPasswordResetToken(user.getUserId());

        // 재설정 링크 이메일 발송
        String resetLink = frontUrl + "/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getUserEmail(), resetLink);

        log.info("비밀번호 재설정 링크 발송 완료: userId={}, link={}", user.getUserId(), resetLink);
    }

    /** 비밀번호 재설정: 토큰 검증 후 새 비밀번호 저장 + 계정 잠금/실패횟수 해제 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordRequest request) {
        // 1. 토큰 검증 (만료·서명·purpose 확인)
        String userId = jwtTokenProvider.validatePasswordResetToken(request.token());

        // 2. 사용자 존재 확인
        userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 3. 새 비밀번호 암호화 저장
        userMapper.updatePassword(userId, passwordEncoder.encode(request.newPassword()));

        // 4. 계정 잠금/실패횟수 초기화 — 5회 잠금 사용자도 즉시 로그인 가능
        userMapper.unlockAccount(userId);

        log.info("비밀번호 재설정 완료 (계정 잠금 해제 포함): userId={}", userId);
    }

    /** 아이디 중복 확인 */
    @Override
    @Transactional(readOnly = true)
    public boolean checkDuplicate(String userId) {
        return userMapper.existsByUserId(userId);
    }

    /** 전화번호 중복 확인 */
    @Override
    @Transactional(readOnly = true)
    public boolean checkPhoneDuplicate(String mobileNo) {
        return userMapper.existsByMobileNo(mobileNo);
    }

    /** 소셜 연동용 기존 계정 확인 (이름+전화번호) */
    @Override
    @Transactional(readOnly = true)
    public FindIdResponse findByNameAndPhoneForSocial(String userName, String mobileNo) {
        return userMapper.findByNameAndPhone(userName, mobileNo)
                .map(user -> FindIdResponse.builder()
                        .maskedUserId(maskEmail(user.getUserId()))
                        .registeredDate(user.getCreateAt() != null
                                ? user.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                : "")
                        .build())
                .orElse(null);
    }

    /** 이메일 마스킹 (ho****ng@gmail.com) */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        String[] parts = email.split("@");
        String local = parts[0];
        if (local.length() <= 2) {
            return local.charAt(0) + "***@" + parts[1];
        }
        int visibleLen = Math.min(2, local.length() / 3);
        String start = local.substring(0, visibleLen);
        String end = local.substring(local.length() - visibleLen);
        return start + "****" + end + "@" + parts[1];
    }
}
