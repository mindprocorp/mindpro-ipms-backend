package kr.co.mindpro.ipms.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "아이디 찾기 응답")
public record FindIdResponse(
    @Schema(description = "마스킹된 아이디(이메일)", example = "ho****ng@gmail.com")
    String maskedUserId,

    @Schema(description = "가입일", example = "2026-01-15")
    String registeredDate
) {}
