package kr.co.mindpro.ipms.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "아이디 찾기 요청")
public record FindIdRequest(
    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "사용자 이름", example = "홍길동")
    String userNameKo,

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Schema(description = "휴대폰 번호", example = "01012345678")
    String userMobileNo
) {}
