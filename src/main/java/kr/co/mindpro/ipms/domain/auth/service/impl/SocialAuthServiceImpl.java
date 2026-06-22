package kr.co.mindpro.ipms.domain.auth.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse;
import kr.co.mindpro.ipms.domain.auth.dto.response.SocialLoginResponse;
import kr.co.mindpro.ipms.domain.auth.repository.db1.SocialAuthMapper;
import kr.co.mindpro.ipms.domain.auth.service.SocialAuthService;
import kr.co.mindpro.ipms.domain.auth.service.social.SocialAuthProvider;
import kr.co.mindpro.ipms.domain.auth.service.social.SocialUserInfo;
import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthMappVO;
import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthVO;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialAuthServiceImpl implements SocialAuthService {

    private final SocialAuthMapper socialAuthMapper;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final List<SocialAuthProvider> providers;

    @Value("${jwt.expiration}")
    private long accessTokenValidityInMilliseconds;

    private Map<String, SocialAuthProvider> providerMap;

    @PostConstruct
    private void initProviderMap() {
        providerMap = providers.stream()
            .collect(Collectors.toMap(SocialAuthProvider::getProviderName, Function.identity()));
    }

    @Override
    @Transactional
    public SocialLoginResponse socialLogin(String providerName, String code, String redirectUri) {
        // 1. 프로바이더 조회
        SocialAuthProvider provider = providerMap.get(providerName.toUpperCase());
        if (provider == null) {
            throw new BusinessException("지원하지 않는 소셜 로그인입니다: " + providerName, ErrorCode.AUTH_FAILED);
        }

        // 2. 소셜 사용자 정보 조회 (인가코드 → 토큰 교환 → 유저 정보)
        SocialUserInfo userInfo = provider.getUserInfo(code, redirectUri);

        log.info("소셜 로그인 시도: provider={}, id={}, email={}",
            userInfo.getProvider(), userInfo.getProviderId(), userInfo.getEmail());

        // 3. 소셜 매핑 테이블에서 기존 연동 확인
        Optional<SocialAuthMappVO> existingMapp = socialAuthMapper.findMappByProviderAndId(
            userInfo.getProvider(), userInfo.getProviderId()
        );

        if (existingMapp.isPresent()) {
            // 기존 연동 있음 → 토큰 갱신 후 로그인
            socialAuthMapper.updateSocialAuthToken(
                userInfo.getProvider(), userInfo.getProviderId(), userInfo.getAccessToken()
            );
            return loginByUserMstSeq(existingMapp.get().getUserMstSeq(), userInfo);
        }

        // 4. 소셜 매핑 없음 → 기존 계정 확인 페이지로 이동 (연동 의사 확인 필요)
        return SocialLoginResponse.builder()
            .authenticated(false)
            .newUser(true)
            .socialEmail(userInfo.getEmail())
            .socialName(userInfo.getName())
            .provider(userInfo.getProvider())
            .providerId(userInfo.getProviderId())
            .build();
    }

    private SocialLoginResponse loginByUserMstSeq(String userMstSeq, SocialUserInfo userInfo) {
        UserMasterVO user = userMapper.findByUserMstSeq(userMstSeq)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return loginByUser(user, userInfo);
    }

    private SocialLoginResponse loginByUser(UserMasterVO user, SocialUserInfo userInfo) {
        // 계정 상태 확인
        if ("Y".equals(user.getDelYn())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if ("Y".equals(user.getLoginLockYn())) {
            throw new BusinessException(ErrorCode.AUTH_FAILED);
        }

        // [LEGACY] JWT auth claim 발급용 — utb_user_role 기반. (소셜 로그인)
        // 실제 권한은 oe.role_seq + admin_auth 기반. utb_user_role 제거 시 함께 정리.
        List<String> userRoles = userMapper.getUserRoleByMstSeq(user.getUserMstSeq());
        String rolePriority = userRoles.isEmpty() ? "ROLE_GUEST" : userRoles.get(0);

        // JWT 토큰 발급 (기존 로그인과 동일한 5개 파라미터)
        String accessToken = jwtTokenProvider.createToken(
            user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(),
            user.getOfficeSeq(), rolePriority
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(
            user.getUserId(), user.getUserInfoSeq(), user.getUserMstSeq(),
            user.getOfficeSeq(), rolePriority
        );

        LoginResponse loginResponse = LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(accessTokenValidityInMilliseconds / 1000)
            .userInfoSeq(user.getUserInfoSeq())
            .userMstSeq(user.getUserMstSeq())
            .officeSeq(user.getOfficeSeq())
            .userRole(rolePriority)
            .build();

        return SocialLoginResponse.builder()
            .authenticated(true)
            .newUser(false)
            .loginResponse(loginResponse)
            .provider(userInfo.getProvider())
            .providerId(userInfo.getProviderId())
            .build();
    }

    @Override
    @Transactional
    public SocialLoginResponse linkExistingAccount(String userName, String mobileNo,
                                                    String provider, String providerId, String socialEmail) {
        // 이름 + 전화번호로 기존 회원 검색
        Optional<UserMasterVO> existingUser = userMapper.findByNameAndPhone(userName, mobileNo);

        if (existingUser.isEmpty()) {
            return SocialLoginResponse.builder()
                    .authenticated(false)
                    .newUser(true)
                    .provider(provider)
                    .providerId(providerId)
                    .socialEmail(socialEmail)
                    .build();
        }

        UserMasterVO user = existingUser.get();

        // 소셜 연동 저장
        SocialUserInfo userInfo = SocialUserInfo.builder()
                .provider(provider.toUpperCase())
                .providerId(providerId)
                .email(socialEmail)
                .name(userName)
                .accessToken("")
                .build();

        saveSocialAuth(userInfo);
        saveSocialAuthMapp(userInfo, user.getUserMstSeq());

        return loginByUser(user, userInfo);
    }

    private void saveSocialAuth(SocialUserInfo userInfo) {
        SocialAuthVO vo = new SocialAuthVO();
        vo.setAuthSeq(userInfo.getProviderId());
        vo.setAuthApproach(userInfo.getProvider());
        vo.setAuthToken(userInfo.getAccessToken());
        vo.setProviderEmail(userInfo.getEmail());
        vo.setProviderName(userInfo.getName());
        vo.setCreateUser("SYSTEM");
        vo.setUpdateUser("SYSTEM");
        socialAuthMapper.insertSocialAuth(vo);
    }

    private void saveSocialAuthMapp(SocialUserInfo userInfo, String userMstSeq) {
        SocialAuthMappVO mapp = new SocialAuthMappVO();
        mapp.setAuthSeq(userInfo.getProviderId());
        mapp.setAuthApproach(userInfo.getProvider());
        mapp.setUserMstSeq(userMstSeq);
        mapp.setCreateUser("SYSTEM");
        mapp.setUpdateUser("SYSTEM");
        socialAuthMapper.insertSocialAuthMapp(mapp);
    }
}
