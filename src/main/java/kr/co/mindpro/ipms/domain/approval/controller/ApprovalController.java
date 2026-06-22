package kr.co.mindpro.ipms.domain.approval.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.approval.service.ApprovalService;
import kr.co.mindpro.ipms.domain.approval.vo.ApprDocVO;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "결재 API", description = "기안 작성/조회, 결재 처리 API")
@RestController
@RequestMapping("/api/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    // ── 기안 저장 (임시저장 / 상신) ──────────────────────────

    @Operation(summary = "기안 저장 (임시저장/상신)")
    @PostMapping("/doc")
    public ResponseEntity<ApiResponse<ApprDocVO>> saveDoc(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ApprDocVO vo) {
        vo.setOfficeSeq(user.getOfficeSeq());
        vo.setDrafterSeq(user.getUserMstSeq());
        vo.setCreateUser(user.getUserMstSeq());
        vo.setUpdateUser(user.getUserMstSeq());
        ApprDocVO saved = approvalService.saveDoc(vo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "저장되었습니다.", saved));
    }

    // ── 기안 상세 조회 ─────────────────────────────────────

    @Operation(summary = "기안 문서 상세 조회")
    @GetMapping("/doc/{docSeq}")
    public ResponseEntity<ApiResponse<ApprDocVO>> getDoc(@PathVariable String docSeq) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공", approvalService.getDoc(docSeq)));
    }

    // ── 내 기안함 ───────────────────────────────────────────

    @Operation(summary = "내 기안함 목록")
    @GetMapping("/doc/my")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getMyDraftList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String docStatus,
            @RequestParam(required = false) String keyword) {
        List<ApprDocVO> list = approvalService.getMyDraftList(user.getUserMstSeq(), docStatus, keyword);
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공", list));
    }

    // ── 결재 대기함 ─────────────────────────────────────────

    @Operation(summary = "결재 대기함")
    @GetMapping("/doc/pending")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getPendingList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공",
                approvalService.getPendingList(user.getUserMstSeq(), keyword)));
    }

    // ── 결재 참조/열람 ──────────────────────────────────────

    @Operation(summary = "결재 참조/열람")
    @GetMapping("/doc/reference")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getReferenceList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공",
                approvalService.getReferenceList(user.getUserMstSeq(), user.getOfficeSeq(), keyword)));
    }

    // ── 부서 수신함 ─────────────────────────────────────────

    @Operation(summary = "부서 수신함")
    @GetMapping("/doc/dept-inbox")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getDeptInboxList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공",
                approvalService.getDeptInboxList(user.getUserMstSeq(), keyword)));
    }

    // ── 부서 참조/열람 ──────────────────────────────────────

    @Operation(summary = "부서 참조/열람")
    @GetMapping("/doc/dept-reference")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getDeptReferenceList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공",
                approvalService.getDeptReferenceList(user.getUserMstSeq(), user.getOfficeSeq(), keyword)));
    }

    // ── 문서 수신함 ─────────────────────────────────────────

    @Operation(summary = "문서 수신함")
    @GetMapping("/doc/inbox")
    public ResponseEntity<ApiResponse<List<ApprDocVO>>> getInboxList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(200, "조회 성공",
                approvalService.getInboxList(user.getUserMstSeq(), keyword)));
    }

    // ── 결재 처리 (승인/반려) ───────────────────────────────

    @Operation(summary = "결재 처리 (승인/반려)")
    @PutMapping("/doc/{docSeq}/line/{lineSeq}/action")
    public ResponseEntity<ApiResponse<Void>> processApproval(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String docSeq,
            @PathVariable String lineSeq,
            @RequestBody Map<String, String> body) {
        String action = body.get("action");   // APPROVED | REJECTED
        String comment = body.get("comment");
        if (!"APPROVED".equals(action) && !"REJECTED".equals(action)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.success(400, "action은 APPROVED 또는 REJECTED여야 합니다."));
        }
        approvalService.processApproval(docSeq, lineSeq, action, comment, user.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(200,
                "APPROVED".equals(action) ? "승인되었습니다." : "반려되었습니다."));
    }

    // ── 상신 취소 (회수) ─────────────────────────────────────

    @Operation(summary = "상신 취소 (회수)")
    @PutMapping("/doc/{docSeq}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawDoc(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String docSeq) {
        approvalService.withdrawDoc(docSeq, user.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(200, "회수되었습니다."));
    }

    // ── 기안 삭제 ───────────────────────────────────────────

    @Operation(summary = "기안 삭제 (임시저장/회수 상태만)")
    @DeleteMapping("/doc/{docSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteDoc(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String docSeq) {
        approvalService.deleteDoc(docSeq, user.getUserMstSeq());
        return ResponseEntity.ok(ApiResponse.success(200, "삭제되었습니다."));
    }
}
