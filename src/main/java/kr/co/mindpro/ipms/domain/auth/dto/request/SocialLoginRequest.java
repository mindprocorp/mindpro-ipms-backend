package kr.co.mindpro.ipms.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "소셜 로그인 요청")
public record SocialLoginRequest(
    @NotBlank
    @Schema(description = "소셜 인증 후 받은 인가 코드")
    String code,

    @NotBlank
    @Schema(description = "프론트 Redirect URI", example = "http://localhost:4000/auth/kakao/callback")
    String redirectUri
) {}
