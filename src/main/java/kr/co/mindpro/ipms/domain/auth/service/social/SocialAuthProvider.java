package kr.co.mindpro.ipms.domain.auth.service.social;

public interface SocialAuthProvider {

    String getProviderName();

    SocialUserInfo getUserInfo(String code, String redirectUri);
}
