package kr.co.mindpro.ipms.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * [Request] PENDING 직원 승인 시 역할 + 사무소 직원 정보 필수 입력.
 * POST /api/users/approve/{userMstSeq} 의 body.
 */
@Schema(description = "직원 승인 요청")
public record ApproveEmployeeRequest(
        @Schema(description = "역할 일련번호 (필수)") @NotBlank(message = "역할은 필수입니다.") String roleSeq,
        @Schema(description = "직책") String officeEmployeePosition,
        @Schema(description = "부서명 (문자열)") String officeEmployeeDept,
        @Schema(description = "부서 일련번호 (FK)") String deptSeq,
        @Schema(description = "직무 코드") String workCode,
        @Schema(description = "직위 코드") String positionCode,
        @Schema(description = "직급 코드") String jobGradeCode,
        @Schema(description = "사용자 유형 코드") String userTypeCode,
        @Schema(description = "근무 상태 코드") String workStatusCode,
        @Schema(description = "재직 상태 코드") String employStatusCode
) {}
