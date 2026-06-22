package kr.co.mindpro.ipms.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "비밀번호 재설정 요청")
public record ResetPasswordRequest(
    @NotBlank(message = "토큰은 필수입니다.")
    @Schema(description = "비밀번호 재설정 토큰")
    String token,

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
             message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "새 비밀번호", example = "NewPass1!")
    String newPassword
) {}
