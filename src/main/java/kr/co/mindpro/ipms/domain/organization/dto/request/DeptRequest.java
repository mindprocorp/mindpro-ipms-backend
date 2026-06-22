package kr.co.mindpro.ipms.domain.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeptRequest {

    @Schema(description = "부서 등록 요청")
    public record Create(
        @Schema(description = "사무소 일련번호", example = "PGOKR20260000002")
        String officeSeq,

        @Schema(description = "상위 부서 일련번호 (최상위면 비워두세요)")
        String parentDeptSeq,

        @Schema(description = "부서 코드", example = "PATENT1")
        String deptCode,

        @Schema(description = "부서명", example = "특허1팀")
        String deptName,

        @Schema(description = "정렬 순서", example = "1")
        String sortOrd
    ) {}

    @Schema(description = "부서 수정 요청")
    public record Update(
        @Schema(description = "부서 일련번호", example = "DPTMS20260000001")
        String deptSeq,

        @Schema(description = "상위 부서 일련번호")
        String parentDeptSeq,

        @Schema(description = "부서 코드", example = "PATENT1")
        String deptCode,

        @Schema(description = "부서명", example = "특허1팀")
        String deptName,

        @Schema(description = "정렬 순서", example = "1")
        String sortOrd,

        @Schema(description = "사용 여부", example = "Y")
        String useYn
    ) {}
}
