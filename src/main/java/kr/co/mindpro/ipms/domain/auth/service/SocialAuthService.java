package kr.co.mindpro.ipms.domain.auth.service;

import kr.co.mindpro.ipms.domain.auth.dto.response.SocialLoginResponse;

public interface SocialAuthService {

    SocialLoginResponse socialLogin(String providerName, String code, String redirectUri);

    SocialLoginResponse linkExistingAccount(String userName, String mobileNo,
                                            String provider, String providerId, String socialEmail);
}
