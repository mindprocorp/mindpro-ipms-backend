package kr.co.mindpro.ipms.domain.organization.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.organization.dto.request.DeptRequest;
import kr.co.mindpro.ipms.domain.organization.service.OrganizationService;
import kr.co.mindpro.ipms.domain.registry.repository.db1.OfficeMapper;
import kr.co.mindpro.ipms.domain.organization.vo.ApprTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.DeptVO;
import kr.co.mindpro.ipms.domain.organization.vo.FormTemplateVO;
import kr.co.mindpro.ipms.domain.organization.vo.OfficeCodeVO;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "조직관리 API", description = "부서 및 사무소 코드 관리 API")
@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OfficeMapper officeMapper;

    @Operation(summary = "초대코드 조회")
    @GetMapping("/invite-code")
    public ResponseEntity<ApiResponse<String>> getInviteCode(@AuthenticationPrincipal CustomUserDetails user) {
        String officeSeq = user.getOfficeSeq();
        return officeMapper.findAllActiveOffices().stream()
                .filter(o -> o.getOfficeSeq().equals(officeSeq))
                .findFirst()
                .map(o -> ResponseEntity.ok(ApiResponse.success(200, "조회 성공", o.getOfficeInviteCode())))
                .orElse(ResponseEntity.ok(ApiResponse.success(200, "조회 성공", (String) null)));
    }

    @Operation(summary = "초대코드 재발급")
    @PutMapping("/invite-code")
    public ResponseEntity<ApiResponse<String>> regenerateInviteCode(@AuthenticationPrincipal CustomUserDetails user) {
        String officeSeq = user.getOfficeSeq();
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.security.SecureRandom();
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        String newCode = sb.toString();
        officeMapper.updateInviteCode(officeSeq, newCode);
        return ResponseEntity.ok(ApiResponse.success(200, "재발급 성공", newCode));
    }

    @Operation(summary = "부서 트리 조회", description = "사무소별 부서 트리를 조회합니다.")
    @GetMapping("/dept/tree")
    public ResponseEntity<ApiResponse<List<DeptVO>>> getDeptTree(
            @AuthenticationPrincipal CustomUserDetails user) {
        List<DeptVO> tree = organizationService.getDeptTree(user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "부서 트리 조회 성공", tree));
    }

    @Operation(summary = "부서 등록")
    @PostMapping("/dept")
    public ResponseEntity<ApiResponse<Void>> createDept(@AuthenticationPrincipal CustomUserDetails user, @RequestBody DeptRequest.Create request) {
        DeptVO vo = new DeptVO();
        vo.setOfficeSeq(user.getOfficeSeq());
        vo.setParentDeptSeq(request.parentDeptSeq() != null && !request.parentDeptSeq().isBlank()
                ? request.parentDeptSeq() : null);
        vo.setDeptCode(request.deptCode());
        vo.setDeptName(request.deptName());
        vo.setSortOrd(request.sortOrd() != null ? request.sortOrd() : "0");
        vo.setCreateUser("SYSTEM");
        vo.setUpdateUser("SYSTEM");

        organizationService.createDept(vo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "부서가 등록되었습니다."));
    }

    @Operation(summary = "부서 수정")
    @PutMapping("/dept")
    public ResponseEntity<ApiResponse<Void>> updateDept(@RequestBody DeptRequest.Update request) {
        DeptVO vo = new DeptVO();
        vo.setDeptSeq(request.deptSeq());
        vo.setParentDeptSeq(request.parentDeptSeq() != null && !request.parentDeptSeq().isBlank()
                ? request.parentDeptSeq() : null);
        vo.setDeptCode(request.deptCode());
        vo.setDeptName(request.deptName());
        vo.setSortOrd(request.sortOrd());
        vo.setUseYn(request.useYn());
        vo.setUpdateUser("SYSTEM");

        organizationService.updateDept(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "부서가 수정되었습니다."));
    }

    @Operation(summary = "부서 삭제 (논리)")
    @DeleteMapping("/dept/{deptSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteDept(
            @Parameter(description = "부서 일련번호", example = "DPTMS20260000001")
            @PathVariable String deptSeq) {
        organizationService.deleteDept(deptSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "부서가 삭제되었습니다."));
    }

    @Operation(summary = "사무소별 코드 조회", description = "코드 분류와 사무소별 코드 목록을 조회합니다.")
    @GetMapping("/code")
    public ResponseEntity<ApiResponse<List<OfficeCodeVO>>> getOfficeCodeList(
            @Parameter(description = "코드 분류", example = "JOB_GRADE")
            @RequestParam String codeClass,
            @AuthenticationPrincipal CustomUserDetails user) {
        List<OfficeCodeVO> list = organizationService.getOfficeCodeList(codeClass, user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "코드 조회 성공", list));
    }

    @Operation(summary = "사무소별 코드 자동 등록", description = "코드명이 없으면 자동 등록하고 반환합니다.")
    @PostMapping("/code/auto")
    public ResponseEntity<ApiResponse<OfficeCodeVO>> getOrCreateCode(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "코드 분류", example = "JOB_GRADE")
            @RequestParam String codeClass,
            @Parameter(description = "코드명", example = "시니어 변리사")
            @RequestParam String codeName) {
        OfficeCodeVO code = organizationService.getOrCreateOfficeCode(user.getOfficeSeq(), codeClass, codeName);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "코드 조회/등록 성공", code));
    }

    @Operation(summary = "사무소별 코드 수정")
    @PutMapping("/code")
    public ResponseEntity<ApiResponse<Void>> updateOfficeCode(@RequestBody OfficeCodeVO vo) {
        organizationService.updateOfficeCode(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "코드가 수정되었습니다."));
    }

    @Operation(summary = "사무소별 코드 삭제 (논리)")
    @DeleteMapping("/code/{officeCodeSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteOfficeCode(
            @Parameter(description = "코드 일련번호", example = "OFCCD20260000001")
            @PathVariable String officeCodeSeq) {
        organizationService.deleteOfficeCode(officeCodeSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "코드가 삭제되었습니다."));
    }

    // ─── 결재선 템플릿 ──────────────────────────────

    @Operation(summary = "결재선 템플릿 목록 조회")
    @GetMapping("/appr-template")
    public ResponseEntity<ApiResponse<List<ApprTemplateVO>>> getApprTemplateList(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "결재선명 검색")
            @RequestParam(required = false) String templateName) {
        List<ApprTemplateVO> list = organizationService.getApprTemplateList(user.getOfficeSeq(), templateName);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "결재선 템플릿 조회 성공", list));
    }

    @Operation(summary = "결재선 템플릿 상세 조회 (단계 포함)")
    @GetMapping("/appr-template/{apprTemplateSeq}")
    public ResponseEntity<ApiResponse<ApprTemplateVO>> getApprTemplateDetail(
            @Parameter(description = "결재선 템플릿 일련번호", example = "APTPL20260000001")
            @PathVariable String apprTemplateSeq) {
        ApprTemplateVO detail = organizationService.getApprTemplateDetail(apprTemplateSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "결재선 템플릿 상세 조회 성공", detail));
    }

    @Operation(summary = "결재선 템플릿 등록/수정")
    @PostMapping("/appr-template")
    public ResponseEntity<ApiResponse<Void>> saveApprTemplate(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ApprTemplateVO vo) {
        vo.setOfficeSeq(user.getOfficeSeq());
        organizationService.saveApprTemplate(vo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "결재선 템플릿이 저장되었습니다."));
    }

    @Operation(summary = "결재선 템플릿 삭제")
    @DeleteMapping("/appr-template/{apprTemplateSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteApprTemplate(
            @Parameter(description = "결재선 템플릿 일련번호", example = "APTPL20260000001")
            @PathVariable String apprTemplateSeq) {
        organizationService.deleteApprTemplate(apprTemplateSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "결재선 템플릿이 삭제되었습니다."));
    }

    // ─── 서식 템플릿 ────────────────────────────────

    @Operation(summary = "서식 템플릿 목록 조회")
    @GetMapping("/form-template")
    public ResponseEntity<ApiResponse<List<FormTemplateVO>>> getFormTemplateList(
            @AuthenticationPrincipal CustomUserDetails user,
            @Parameter(description = "카테고리 코드")
            @RequestParam(required = false) String categoryCode,
            @Parameter(description = "서식명 검색")
            @RequestParam(required = false) String templateName) {
        List<FormTemplateVO> list = organizationService.getFormTemplateList(user.getOfficeSeq(), categoryCode, templateName, user.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "서식 템플릿 조회 성공", list));
    }

    @Operation(summary = "서식 템플릿 상세 조회 (JSON 포함)")
    @GetMapping("/form-template/{formTemplateSeq}")
    public ResponseEntity<ApiResponse<FormTemplateVO>> getFormTemplateDetail(
            @Parameter(description = "서식 템플릿 일련번호", example = "FMTPL20260000001")
            @PathVariable String formTemplateSeq) {
        FormTemplateVO detail = organizationService.getFormTemplateDetail(formTemplateSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "서식 템플릿 상세 조회 성공", detail));
    }

    @Operation(summary = "서식 템플릿 등록/수정")
    @PostMapping("/form-template")
    public ResponseEntity<ApiResponse<Void>> saveFormTemplate(@RequestBody FormTemplateVO vo) {
        organizationService.saveFormTemplate(vo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "서식 템플릿이 저장되었습니다."));
    }

    @Operation(summary = "서식 템플릿 삭제")
    @DeleteMapping("/form-template/{formTemplateSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteFormTemplate(
            @Parameter(description = "서식 템플릿 일련번호", example = "FMTPL20260000001")
            @PathVariable String formTemplateSeq) {
        organizationService.deleteFormTemplate(formTemplateSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "서식 템플릿이 삭제되었습니다."));
    }
}
