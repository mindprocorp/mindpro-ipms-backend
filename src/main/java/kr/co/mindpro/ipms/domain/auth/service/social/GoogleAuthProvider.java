package kr.co.mindpro.ipms.domain.auth.service.social;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GoogleAuthProvider implements SocialAuthProvider {

    @Value("${social.google.client-id}")
    private String clientId;

    @Value("${social.google.client-secret}")
    private String clientSecret;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getProviderName() {
        return "GOOGLE";
    }

    @Override
    public SocialUserInfo getUserInfo(String code, String redirectUri) {
        String accessToken = getAccessToken(code, redirectUri);
        return fetchUserInfo(accessToken);
    }

    private String getAccessToken(String code, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, request, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.get("access_token").asText();
        } catch (Exception e) {
            log.error("구글 토큰 교환 실패: {}", e.getMessage());
            throw new BusinessException("구글 인증에 실패했습니다.", ErrorCode.AUTH_FAILED);
        }
    }

    private SocialUserInfo fetchUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                USER_INFO_URL, HttpMethod.GET, request, String.class
            );
            JsonNode root = objectMapper.readTree(response.getBody());

            String id = root.get("id").asText();
            String email = root.has("email") ? root.get("email").asText() : null;
            String name = root.has("name") ? root.get("name").asText() : null;

            return SocialUserInfo.builder()
                .provider("GOOGLE")
                .providerId(id)
                .email(email)
                .name(name)
                .accessToken(accessToken)
                .build();
        } catch (Exception e) {
            log.error("구글 사용자 정보 조회 실패: {}", e.getMessage());
            throw new BusinessException("구글 사용자 정보를 가져올 수 없습니다.", ErrorCode.AUTH_FAILED);
        }
    }
}
