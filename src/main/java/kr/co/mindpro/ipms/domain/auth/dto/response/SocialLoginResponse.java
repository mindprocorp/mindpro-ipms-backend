package kr.co.mindpro.ipms.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "소셜 로그인 응답")
public record SocialLoginResponse(
    @Schema(description = "로그인 성공 여부")
    boolean authenticated,

    @Schema(description = "신규 회원 여부 (true면 회원가입 필요)")
    boolean newUser,

    @Schema(description = "로그인 응답 (기존 회원인 경우)")
    LoginResponse loginResponse,

    @Schema(description = "소셜 이메일 (신규 회원인 경우 회원가입 폼 자동채움용)")
    String socialEmail,

    @Schema(description = "소셜 이름 (신규 회원인 경우 회원가입 폼 자동채움용)")
    String socialName,

    @Schema(description = "소셜 제공자", example = "KAKAO")
    String provider,

    @Schema(description = "소셜 고유 ID")
    String providerId
) {}
