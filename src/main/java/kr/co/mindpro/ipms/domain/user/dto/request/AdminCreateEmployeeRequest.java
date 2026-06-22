package kr.co.mindpro.ipms.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * [Request] 관리자 직접 등록 — 가입 정보 + 조직/역할/상태를 한 번에.
 * POST /api/users/admin/employee
 *
 * RegisterIndividual 와 분리 이유: 자가 가입(public)에서는 노출돼선 안 되는 필드(roleSeq 등) 차단.
 */
@Schema(description = "관리자 직접 직원 등록 요청")
public record AdminCreateEmployeeRequest(
        // ── 계정 ──
        @NotBlank @Email @Schema(description = "이메일 (ID)") String userEmail,
        @NotBlank @Schema(description = "임시 비밀번호") String userPassword,
        @NotBlank @Schema(description = "성명") String userName,
        @Schema(description = "휴대폰") String mobileNo,
        @NotBlank @Schema(description = "회원 구분", example = "INDIVIDUAL") String userCategoryCode,
        @Schema(description = "소속 사무소 ID (= 등록 관리자의 사무소)") String officeId,

        // ── 약관 (관리자 등록은 자동 동의) ──
        @Schema(description = "이용약관 동의") boolean termsAgree,
        @Schema(description = "개인정보 처리방침 동의") boolean privacyPolicyAgree,
        @Schema(description = "마케팅 수신 동의") boolean marketingAgree,

        // ── 조직 정보 ──
        @Schema(description = "역할 일련번호 (선택)") String roleSeq,
        @Schema(description = "직책 (텍스트)") String officeEmployeePosition,
        @Schema(description = "부서명 (텍스트)") String officeEmployeeDept,
        @Schema(description = "직위 코드") String positionCode,
        @Schema(description = "직급 코드") String jobGradeCode,
        @Schema(description = "직무 코드") String workCode,

        // ── 상태 (utb_user_info) ──
        @Schema(description = "사용자 유형 코드") String userTypeCode,
        @Schema(description = "근무 상태 코드") String workStatusCode,
        @Schema(description = "재직 상태 코드") String employStatusCode
) {}
