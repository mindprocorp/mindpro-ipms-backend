package kr.co.mindpro.ipms.domain.system.service.impl;

import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;
import kr.co.mindpro.ipms.domain.system.repository.db1.SystemMapper;
import kr.co.mindpro.ipms.domain.system.service.SystemService;
import kr.co.mindpro.ipms.domain.system.vo.MenuVO;
import kr.co.mindpro.ipms.domain.system.vo.PlanVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemServiceImpl implements SystemService {

    private final SystemMapper systemMapper;

    @Override
    public List<MenuVO> getAllMenus() {
        return systemMapper.findAllMenus();
    }

    @Override
    public List<MenuVO> getMenusByOffice(String officeSeq) {
        return systemMapper.findMenusByOffice(officeSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(MenuVO vo) {
        // 1) 필수 검증
        if (vo.getMenuNm() == null || vo.getMenuNm().isBlank()) {
            throw new IllegalArgumentException("메뉴명은 필수입니다.");
        }
        if (vo.getMenuCd() == null || vo.getMenuCd().isBlank()) {
            throw new IllegalArgumentException("메뉴 코드는 필수입니다.");
        }
        // 2) 메뉴 코드 중복 체크 (자기 자신 제외)
        if (systemMapper.existsMenuCd(vo.getMenuCd(), vo.getMenuSeq())) {
            throw new IllegalArgumentException("이미 사용 중인 메뉴 코드입니다.");
        }
        // 3) 타입별 URL 정규화
        if ("FOLDER".equals(vo.getMenuType())) {
            vo.setMenuUrl(null);
        } else {
            if (vo.getMenuUrl() == null || vo.getMenuUrl().isBlank()) {
                throw new IllegalArgumentException("페이지 메뉴는 경로 URL이 필수입니다.");
            }
        }
        // 4) 수정: 자식 있는 메뉴는 PAGE로 변경 불가
        boolean isUpdate = vo.getMenuSeq() != null && !vo.getMenuSeq().isBlank();
        if (isUpdate && "PAGE".equals(vo.getMenuType()) && systemMapper.countChildren(vo.getMenuSeq()) > 0) {
            throw new IllegalStateException("하위 메뉴가 있어 페이지로 변경할 수 없습니다.");
        }

        // 5) [방어] 수정 시 menuCd 변경 금지 (자식 prefix·역할매핑 정합성 깨짐 방지)
        if (isUpdate) {
            MenuVO existing = systemMapper.findAllMenus().stream()
                    .filter(m -> m.getMenuSeq().equals(vo.getMenuSeq()))
                    .findFirst().orElse(null);
            if (existing != null && existing.getMenuCd() != null
                    && !existing.getMenuCd().equals(vo.getMenuCd())) {
                throw new IllegalStateException("메뉴 코드는 수정할 수 없습니다.");
            }
        }

        if (isUpdate) {
            systemMapper.updateMenu(vo);
        } else {
            systemMapper.insertMenu(vo); // selectKey로 fn_get_dynamic_seq 자동 채번
        }

        // [자동 전파] 루트이면서 super_admin_only 지정된 경우 자손 트리 전체에 전파
        if ((vo.getParentMenuSeq() == null || vo.getParentMenuSeq().isBlank())
                && vo.getSuperAdminOnly() != null
                && vo.getMenuSeq() != null && !vo.getMenuSeq().isBlank()) {
            systemMapper.propagateSuperAdminOnly(vo.getMenuSeq(), vo.getSuperAdminOnly());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String menuSeq) {
        // 하위 메뉴 존재 시 삭제 차단
        if (systemMapper.countChildren(menuSeq) > 0) {
            throw new IllegalStateException("하위 메뉴가 있어 삭제할 수 없습니다.");
        }
        systemMapper.deleteMenu(menuSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reorder(String target, List<String> orderedSeqs) {
        for (int i = 0; i < orderedSeqs.size(); i++) {
            int order = (i + 1) * 10;
            switch (target) {
                case "MENU" -> systemMapper.updateMenuOrder(orderedSeqs.get(i), order);
                case "OFFICE_CODE" -> systemMapper.updateOfficeCodeOrder(orderedSeqs.get(i), order);
                case "DEPT" -> systemMapper.updateDeptOrder(orderedSeqs.get(i), order);
                case "FORM_TEMPLATE" -> systemMapper.updateFormTemplateOrder(orderedSeqs.get(i), order);
                default -> throw new IllegalArgumentException("지원하지 않는 대상: " + target);
            }
        }
    }

    @Override
    public List<RoleVO> getAllRoles(String officeSeq) {
        return systemMapper.findAllRoles(officeSeq);
    }

    @Override
    public List<RoleVO> getActiveRoles(String officeSeq) {
        return systemMapper.findActiveRoles(officeSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(RoleVO vo) {
        if (vo.getRoleSeq() == null || vo.getRoleSeq().isBlank()) {
            // [방어] 시스템 역할(SUPER_ADMIN/SYSTEM_ADMIN/SYSTEM_USER)은 외부 생성 금지 — CUSTOM만 생성 가능
            if (vo.getRoleType() != null && !"CUSTOM".equals(vo.getRoleType())) {
                throw new IllegalStateException("시스템 기본 역할은 외부에서 생성할 수 없습니다.");
            }
            // 명시 안 했으면 CUSTOM으로 강제 (mapper insert도 COALESCE 'CUSTOM' 처리)
            vo.setRoleType("CUSTOM");
            // roleSeq는 selectKey(fn_get_dynamic_seq)로 자동 생성
            if (vo.getRoleCd() == null || vo.getRoleCd().isBlank()) {
                String officeHash = String.format("%04X",
                        Math.abs(vo.getOfficeSeq() != null ? vo.getOfficeSeq().hashCode() : 0) % 0xFFFF);
                String prefix = "ROLE_" + officeHash;
                int nextSeq = systemMapper.findMaxRoleCdSeq(prefix) + 1;
                vo.setRoleCd(String.format("%s_%04d", prefix, nextSeq));
            }
            systemMapper.insertRole(vo);
        } else {
            // [방어] 기존 SUPER_ADMIN 역할의 수정 차단
            RoleVO existing = systemMapper.findRoleBySeq(vo.getRoleSeq());
            if (existing != null && "SUPER_ADMIN".equals(existing.getRoleType())) {
                throw new IllegalStateException("슈퍼어드민 역할은 수정할 수 없습니다.");
            }
            systemMapper.updateRole(vo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleSeq) {
        RoleVO role = systemMapper.findRoleBySeq(roleSeq);
        if (role != null && !"CUSTOM".equals(role.getRoleType())) {
            throw new IllegalStateException("시스템 기본 역할은 삭제할 수 없습니다.");
        }
        systemMapper.deleteRole(roleSeq);
    }

    @Override
    public List<RoleMenuMapVO> getRoleMenus(String roleSeq) {
        RoleVO role = systemMapper.findRoleBySeq(roleSeq);
        if (role == null) return List.of();

        // 시스템 역할은 DB 매핑 없이 활성 메뉴 기반 자동 계산
        if ("SYSTEM_ADMIN".equals(role.getRoleType())) {
            return systemMapper.findAllActiveMenus().stream()
                    .map(m -> buildPerm(roleSeq, m.getMenuSeq(), "Y", "Y", "Y", "Y"))
                    .toList();
        }
        // CUSTOM: 모든 active 메뉴를 base로 응답 (매핑된 권한이 있으면 그 값, 없으면 N)
        // → 프론트는 응답을 그대로 표시하면 되며, 누락 메뉴 채우기 우회 불필요
        java.util.Map<String, RoleMenuMapVO> mapped = systemMapper.findRoleMenus(roleSeq).stream()
                .collect(java.util.stream.Collectors.toMap(RoleMenuMapVO::getMenuSeq, m -> m, (a, b) -> a));
        return systemMapper.findAllActiveMenus().stream()
                .map(m -> mapped.getOrDefault(m.getMenuSeq(),
                        buildPerm(roleSeq, m.getMenuSeq(), "N", "N", "N", "N")))
                .toList();
    }

    private RoleMenuMapVO buildPerm(String roleSeq, String menuSeq, String read, String write, String delete, String excel) {
        RoleMenuMapVO vo = new RoleMenuMapVO();
        vo.setRoleSeq(roleSeq);
        vo.setMenuSeq(menuSeq);
        vo.setCanRead(read);
        vo.setCanWrite(write);
        vo.setCanDelete(delete);
        vo.setCanExcel(excel);
        return vo;
    }

    @Override
    public List<RoleMenuMapVO> getAllMenusAsAdmin() {
        return systemMapper.findAllMenusAsAdmin();
    }

    @Override
    public List<RoleMenuMapVO> getAuthorizedMenus(String roleSeq) {
        RoleVO role = systemMapper.findRoleBySeq(roleSeq);
        if (role == null) return List.of();

        // [추가 분기 — 기존 로직 무개입 원칙] SUPER_ADMIN만 최상단 분기로 얹기
        // 슈퍼어드민 전용 메뉴(super_admin_only='Y') 포함된 전체 메뉴 반환
        if ("SUPER_ADMIN".equals(role.getRoleType())) {
            return systemMapper.findAllMenusAsSuperAdmin();
        }
        if ("SYSTEM_ADMIN".equals(role.getRoleType())) {
            return systemMapper.findAllActiveMenus().stream()
                    .map(m -> buildAuthorizedPerm(m, "Y", "Y", "Y", "Y"))
                    .toList();
        }
        // CUSTOM 역할: DB 매핑 기반 (SYSTEM_USER 디폴트 개념 폐지)
        return systemMapper.findAuthorizedMenuCodes(roleSeq);
    }

    private RoleMenuMapVO buildAuthorizedPerm(MenuVO m, String read, String write, String delete, String excel) {
        RoleMenuMapVO vo = new RoleMenuMapVO();
        vo.setMenuSeq(m.getMenuSeq());
        vo.setMenuCd(m.getMenuCd());
        vo.setMenuNm(m.getMenuNm());
        vo.setParentMenuSeq(m.getParentMenuSeq());
        vo.setMenuUrl(m.getMenuUrl());
        vo.setMenuIcon(m.getMenuIcon());
        vo.setDispOrd(m.getDispOrd());
        vo.setCanRead(read);
        vo.setCanWrite(write);
        vo.setCanDelete(delete);
        vo.setCanExcel(excel);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(String roleSeq, List<RoleMenuMapVO> menus) {
        // 시스템 역할(SYSTEM_ADMIN)은 권한이 자동 계산되므로 매핑 저장 거부
        RoleVO role = systemMapper.findRoleBySeq(roleSeq);
        if (role == null) {
            throw new IllegalArgumentException("존재하지 않는 역할입니다.");
        }
        if (!"CUSTOM".equals(role.getRoleType())) {
            throw new IllegalStateException("시스템 기본 역할의 메뉴 권한은 변경할 수 없습니다.");
        }

        systemMapper.deleteRoleMenus(roleSeq);
        if (menus == null) return;

        // [방어] 슈퍼어드민 전용 메뉴는 일반 역할에 할당 불가 — 무시하고 패스
        java.util.Set<String> superOnly = new java.util.HashSet<>(systemMapper.findSuperAdminOnlyMenuSeqs());

        for (RoleMenuMapVO map : menus) {
            if (map.getMenuSeq() == null || superOnly.contains(map.getMenuSeq())) continue;
            map.setRoleSeq(roleSeq);
            systemMapper.insertRoleMenu(map);
        }
    }

    @Override
    public List<UserMasterVO> getRoleUsers(String roleSeq, String officeSeq) {
        return systemMapper.findRoleUsers(roleSeq, officeSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(String userMstSeq, String roleSeq, String officeSeq) {
        // [방어] 대상 역할이 SUPER_ADMIN이면 외부 API로의 승격 차단
        RoleVO targetRole = systemMapper.findRoleBySeq(roleSeq);
        if (targetRole != null && "SUPER_ADMIN".equals(targetRole.getRoleType())) {
            throw new IllegalStateException("슈퍼어드민 권한은 일반 경로로 부여할 수 없습니다.");
        }
        checkSystemAdminMinCount(userMstSeq, officeSeq);
        systemMapper.assignUserRole(userMstSeq, roleSeq, officeSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserRole(String userMstSeq, String roleSeq, String officeSeq) {
        checkSystemAdminMinCount(userMstSeq, officeSeq);
        systemMapper.removeUserRole(userMstSeq, roleSeq, officeSeq);
    }

    /** 해당 사용자가 SYSTEM_ADMIN 소속이고 해당 사무소에서 마지막 1명이면 예외 */
    private void checkSystemAdminMinCount(String userMstSeq, String officeSeq) {
        String currentRoleSeq = systemMapper.findUserRoleSeq(userMstSeq, officeSeq);
        if (currentRoleSeq == null) return;
        RoleVO currentRole = systemMapper.findRoleBySeq(currentRoleSeq);
        if (currentRole != null && "SYSTEM_ADMIN".equals(currentRole.getRoleType())) {
            if (systemMapper.countRoleUsers(currentRoleSeq, officeSeq) <= 1) {
                throw new IllegalStateException("시스템관리자는 최소 1명이 필요합니다.");
            }
        }
    }

    // ─── 플랜 관리 ─────────────────────────

    @Override
    public List<PlanVO> getAllPlans() {
        return systemMapper.findAllPlans();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlan(PlanVO vo) {
        if (vo.getPlanNm() == null || vo.getPlanNm().isBlank()) {
            throw new IllegalArgumentException("플랜명은 필수입니다.");
        }
        if (vo.getPlanCd() == null || vo.getPlanCd().isBlank()) {
            throw new IllegalArgumentException("플랜 코드는 필수입니다.");
        }
        if (vo.getPlanSeq() == null || vo.getPlanSeq().isBlank()) {
            systemMapper.insertPlan(vo);
        } else {
            // [방어] 수정 시 planCd 변경 금지 (다른 데이터 정합성 깨짐 방지)
            PlanVO existing = systemMapper.findAllPlans().stream()
                    .filter(p -> p.getPlanSeq().equals(vo.getPlanSeq()))
                    .findFirst().orElse(null);
            if (existing != null && !existing.getPlanCd().equals(vo.getPlanCd())) {
                throw new IllegalStateException("플랜 코드는 수정할 수 없습니다.");
            }
            systemMapper.updatePlan(vo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(String planSeq) {
        systemMapper.deletePlan(planSeq);
    }

    @Override
    public List<String> getPlanAllowedMenuSeqs(String planSeq) {
        return systemMapper.findPlanAllowedMenuSeqs(planSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlanMenus(String planSeq, List<String> menuSeqs, String createUser) {
        if (planSeq == null || planSeq.isBlank()) {
            throw new IllegalArgumentException("플랜 식별자가 필요합니다.");
        }
        systemMapper.deletePlanMenus(planSeq);
        if (menuSeqs == null) return;

        // [방어] 슈퍼어드민 전용 메뉴는 플랜에 할당 불가 — 무시하고 패스
        java.util.Set<String> superOnly = new java.util.HashSet<>(systemMapper.findSuperAdminOnlyMenuSeqs());

        for (String menuSeq : menuSeqs) {
            if (menuSeq == null || menuSeq.isBlank()) continue;
            if (superOnly.contains(menuSeq)) continue;
            systemMapper.insertPlanMenu(planSeq, menuSeq, createUser);
        }
    }

    @Override
    public List<OfficeVO> getOfficesByPlan(String planSeq) {
        return systemMapper.findOfficesByPlan(planSeq);
    }

    @Override
    public List<OfficeVO> getOfficesWithoutPlan() {
        return systemMapper.findOfficesWithoutPlan();
    }

    @Override
    public List<OfficeVO> getAllOfficesForAssign() {
        return systemMapper.findAllOfficesWithPlan();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignOfficePlan(String officeSeq, String planSeq) {
        if (officeSeq == null || officeSeq.isBlank()) {
            throw new IllegalArgumentException("사무소 식별자가 필요합니다.");
        }
        if (planSeq == null || planSeq.isBlank()) {
            throw new IllegalArgumentException("플랜 식별자가 필요합니다.");
        }
        // 존재 검증
        PlanVO targetPlan = systemMapper.findAllPlans().stream()
                .filter(p -> p.getPlanSeq().equals(planSeq))
                .findFirst().orElse(null);
        if (targetPlan == null) {
            throw new IllegalArgumentException("존재하지 않는 플랜입니다.");
        }
        if ("N".equals(targetPlan.getDelYn())) {
            // 정상 (활성 플랜)
        } else if ("Y".equals(targetPlan.getDelYn())) {
            throw new IllegalStateException("삭제된 플랜에는 사무소를 배정할 수 없습니다.");
        }
        systemMapper.assignOfficePlan(officeSeq, planSeq);
    }

    @Override
    public List<UserMasterVO> getUsersByPlan(String planSeq) {
        return systemMapper.findUsersByPlan(planSeq);
    }
}
