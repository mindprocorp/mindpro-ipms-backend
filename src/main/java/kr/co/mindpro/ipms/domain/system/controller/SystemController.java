package kr.co.mindpro.ipms.domain.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;
import kr.co.mindpro.ipms.domain.system.dto.ReorderRequest;
import kr.co.mindpro.ipms.domain.system.service.SystemService;
import kr.co.mindpro.ipms.domain.system.vo.MenuVO;
import kr.co.mindpro.ipms.domain.system.vo.PlanVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "System API", description = "시스템 메뉴 및 권한 관리 API")
@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;

    // --- Menus ---
    @Operation(summary = "메뉴 목록 조회",
        description = "all=true: 전체 메뉴(super_admin_only 포함, 메뉴/플랜 관리 화면용). 기본: SUPER_ADMIN 은 전체, 그 외는 사무소 플랜 매핑 메뉴만.")
    @GetMapping("/menus")
    public ResponseEntity<ApiResponse<List<MenuVO>>> getMenus(
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            kr.co.mindpro.ipms.security.vo.CustomUserDetails user,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") boolean all) {
        // all=true: 메뉴 관리/플랜 관리 화면이 전체 메뉴를 필요로 함 (해당 화면 자체가 super_admin_only 플랜을 통해서만 노출)
        if (all) {
            return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", systemService.getAllMenus()));
        }
        boolean isSuper = user != null && user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
        List<MenuVO> menus = (isSuper || user == null)
                ? systemService.getAllMenus()
                : systemService.getMenusByOffice(user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", menus));
    }

    @Operation(summary = "메뉴 단건 저장")
    @PostMapping("/menus")
    public ResponseEntity<ApiResponse<Void>> saveMenu(@RequestBody MenuVO vo) {
        systemService.saveMenu(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공"));
    }

    @Operation(summary = "공통 순서 일괄 변경 (target: MENU, OFFICE_CODE, DEPT)")
    @PostMapping("/reorder")
    public ResponseEntity<ApiResponse<Void>> reorder(@RequestBody ReorderRequest request) {
        systemService.reorder(request.target(), request.orderedSeqs());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "순서 변경 성공"));
    }

    @Operation(summary = "메뉴 단건 삭제")
    @DeleteMapping("/menus/{menuSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable String menuSeq) {
        systemService.deleteMenu(menuSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공"));
    }

    // --- Roles ---
    @Operation(summary = "권한 목록 조회 (사무소별)")
    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<RoleVO>>> getRoles(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", systemService.getAllRoles(user.getOfficeSeq())));
    }

    @Operation(summary = "사용중인 역할 목록 (선택용)")
    @GetMapping("/roles/active")
    public ResponseEntity<ApiResponse<List<RoleVO>>> getActiveRoles(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", systemService.getActiveRoles(user.getOfficeSeq())));
    }

    @Operation(summary = "권한 단건 저장")
    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<Void>> saveRole(@AuthenticationPrincipal CustomUserDetails user, @RequestBody RoleVO vo) {
        vo.setOfficeSeq(user.getOfficeSeq());
        systemService.saveRole(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공"));
    }

    @Operation(summary = "권한 단건 삭제")
    @DeleteMapping("/roles/{roleSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleSeq) {
        systemService.deleteRole(roleSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공"));
    }

    // --- Role Menu Maps ---
    @Operation(summary = "특정 권한의 메뉴 매핑 조회")
    @GetMapping("/roles/{roleSeq}/menus")
    public ResponseEntity<ApiResponse<List<RoleMenuMapVO>>> getRoleMenus(@PathVariable String roleSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공", systemService.getRoleMenus(roleSeq)));
    }

    @Operation(summary = "특정 권한의 메뉴 매핑 일괄 저장")
    @PostMapping("/roles/{roleSeq}/menus")
    public ResponseEntity<ApiResponse<Void>> saveRoleMenus(
            @PathVariable String roleSeq,
            @RequestBody List<RoleMenuMapVO> menus) {
        systemService.saveRoleMenus(roleSeq, menus);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공"));
    }

    // --- Role Users ---
    @Operation(summary = "역할별 구성원 목록 조회")
    @GetMapping("/roles/{roleSeq}/users")
    public ResponseEntity<ApiResponse<List<UserMasterVO>>> getRoleUsers(
            @PathVariable String roleSeq,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getRoleUsers(roleSeq, user.getOfficeSeq())));
    }

    @Operation(summary = "구성원에게 역할 할당")
    @PatchMapping("/roles/{roleSeq}/users/{userMstSeq}")
    public ResponseEntity<ApiResponse<Void>> assignUserRole(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String roleSeq,
            @PathVariable String userMstSeq) {
        systemService.assignUserRole(userMstSeq, roleSeq, user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "할당 성공"));
    }

    @Operation(summary = "구성원에서 역할 해제")
    @DeleteMapping("/roles/{roleSeq}/users/{userMstSeq}")
    public ResponseEntity<ApiResponse<Void>> removeUserRole(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String roleSeq,
            @PathVariable String userMstSeq) {
        systemService.removeUserRole(userMstSeq, roleSeq, user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "해제 성공"));
    }

    // --- 플랜 관리 (슈퍼어드민) ---

    @Operation(summary = "플랜 목록 조회")
    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<PlanVO>>> getPlans() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getAllPlans()));
    }

    @Operation(summary = "플랜 저장 (신규/수정)")
    @PostMapping("/plans")
    public ResponseEntity<ApiResponse<Void>> savePlan(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody PlanVO vo) {
        if (vo.getPlanSeq() == null || vo.getPlanSeq().isBlank()) {
            vo.setCreateUser(user.getUsername());
        } else {
            vo.setUpdateUser(user.getUsername());
        }
        systemService.savePlan(vo);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공"));
    }

    @Operation(summary = "플랜 삭제")
    @DeleteMapping("/plans/{planSeq}")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable String planSeq) {
        systemService.deletePlan(planSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "삭제 성공"));
    }

    @Operation(summary = "플랜별 허용 메뉴 seq 조회")
    @GetMapping("/plans/{planSeq}/menus")
    public ResponseEntity<ApiResponse<List<String>>> getPlanMenus(@PathVariable String planSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getPlanAllowedMenuSeqs(planSeq)));
    }

    @Operation(summary = "플랜별 허용 메뉴 일괄 저장")
    @PostMapping("/plans/{planSeq}/menus")
    public ResponseEntity<ApiResponse<Void>> savePlanMenus(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String planSeq,
            @RequestBody List<String> menuSeqs) {
        systemService.savePlanMenus(planSeq, menuSeqs, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "저장 성공"));
    }

    @Operation(summary = "플랜에 속한 사무소 목록")
    @GetMapping("/plans/{planSeq}/offices")
    public ResponseEntity<ApiResponse<List<OfficeVO>>> getOfficesByPlan(@PathVariable String planSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getOfficesByPlan(planSeq)));
    }

    @Operation(summary = "플랜에 속한 사용자 목록 (플랜 → 사무소 → 사용자)")
    @GetMapping("/plans/{planSeq}/users")
    public ResponseEntity<ApiResponse<List<UserMasterVO>>> getUsersByPlan(@PathVariable String planSeq) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getUsersByPlan(planSeq)));
    }

    @Operation(summary = "플랜 미배정 사무소 목록")
    @GetMapping("/offices/unassigned")
    public ResponseEntity<ApiResponse<List<OfficeVO>>> getUnassignedOffices() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getOfficesWithoutPlan()));
    }

    @Operation(summary = "모든 사무소 + 현재 플랜 정보 (재배정 모달용)")
    @GetMapping("/offices/for-assign")
    public ResponseEntity<ApiResponse<List<OfficeVO>>> getAllOfficesForAssign() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "조회 성공",
                systemService.getAllOfficesForAssign()));
    }

    @Operation(summary = "사무소에 플랜 배정")
    @PatchMapping("/offices/{officeSeq}/plan/{planSeq}")
    public ResponseEntity<ApiResponse<Void>> assignOfficePlan(
            @PathVariable String officeSeq,
            @PathVariable String planSeq) {
        systemService.assignOfficePlan(officeSeq, planSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "배정 성공"));
    }
}
