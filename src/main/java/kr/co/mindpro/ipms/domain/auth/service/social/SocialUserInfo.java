package kr.co.mindpro.ipms.domain.auth.service.social;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfo {
    private final String provider;
    private final String providerId;
    private final String email;
    private final String name;
    private final String accessToken;
}
