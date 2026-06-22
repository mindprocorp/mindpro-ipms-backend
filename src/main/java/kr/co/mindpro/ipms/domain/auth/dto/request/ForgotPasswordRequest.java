package kr.co.mindpro.ipms.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "비밀번호 찾기 요청")
public record ForgotPasswordRequest(
    @NotBlank(message = "아이디(이메일)는 필수입니다.")
    @Schema(description = "아이디(이메일)", example = "user@example.com")
    String userId,

    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "사용자 이름", example = "홍길동")
    String userNameKo,

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Schema(description = "휴대폰 번호", example = "01012345678")
    String userMobileNo
) {}
