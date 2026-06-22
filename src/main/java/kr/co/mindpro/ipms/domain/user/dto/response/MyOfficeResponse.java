package kr.co.mindpro.ipms.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 현재 로그인 유저가 속한 사무소 1건 (드롭다운 전환 UI용).
 * GET /api/users/my-offices 응답 아이템.
 */
@Builder
@Schema(description = "내 소속 사무소")
public record MyOfficeResponse(
        @Schema(description = "사무소 일련번호 (전환 시 사용)") String officeSeq,
        @Schema(description = "사무소 표시명") String officeShortName,
        @Schema(description = "사업자 인증 여부 (Y=사업자, N=개인)") String officeAuthYn,
        @Schema(description = "이 사무소에서 관리자 여부 (Y/N)") String adminAuth,
        @Schema(description = "이 사무소에서의 역할명 (없으면 null)") String roleNm,
        @Schema(description = "역할 타입 (SUPER_ADMIN/SYSTEM_ADMIN/SYSTEM_USER/CUSTOM/null)") String roleType,
        @Schema(description = "이 사무소 membership 상태 (ACTIVE/PENDING/LOCKED/null)") String acctStatusCode,
        @Schema(description = "현재 JWT의 active 사무소 여부") boolean isCurrent
) {}
