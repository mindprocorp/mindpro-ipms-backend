package kr.co.mindpro.ipms.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 정보
 * record를 사용하여 불변 객체로 정의, 데이터 검증 어노테이션을 포함.
 *
 * @author	 : intst
 * @fileName	 : LoginRequest.java
 * @since	 : 2025. 12. 24.
 */
@Schema(description = "로그인 요청 데이터")
public record LoginRequest(
    @Schema(description = "사용자 아이디", example = "jhson@mindpro.co.kr")
    @NotBlank(message = "아이디는 필수 입력 값입니다.") // Jakarta Validation: 필수값 체크
    String userId,

    @Schema(description = "사용자 비밀번호", example = "1q2w3e4r!")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    String userPw
) {}