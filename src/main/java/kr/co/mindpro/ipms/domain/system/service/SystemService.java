package kr.co.mindpro.ipms.domain.system.service;

import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;
import kr.co.mindpro.ipms.domain.system.vo.MenuVO;
import kr.co.mindpro.ipms.domain.system.vo.PlanVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import java.util.List;

public interface SystemService {
    List<MenuVO> getAllMenus();
    /** [Office] 사무소 플랜에 매핑된 메뉴만 — RoleMngList 트리에서 사용. */
    List<MenuVO> getMenusByOffice(String officeSeq);
    void saveMenu(MenuVO vo);
    void deleteMenu(String menuSeq);

    /** 공통 순서 변경 (target: MENU, OFFICE_CODE, DEPT) */
    void reorder(String target, List<String> orderedSeqs);

    List<RoleVO> getAllRoles(String officeSeq);
    List<RoleVO> getActiveRoles(String officeSeq);
    void saveRole(RoleVO vo);
    void deleteRole(String roleSeq);

    List<RoleMenuMapVO> getRoleMenus(String roleSeq);
    /** 로그인 시 사용자 권한 메뉴 조회 (시스템 역할 자동 계산 포함) */
    List<RoleMenuMapVO> getAuthorizedMenus(String roleSeq);
    /** 시스템 관리자용 전체 메뉴 조회 (모든 권한 Y) */
    List<RoleMenuMapVO> getAllMenusAsAdmin();
    void saveRoleMenus(String roleSeq, List<RoleMenuMapVO> menus);

    List<UserMasterVO> getRoleUsers(String roleSeq, String officeSeq);
    void assignUserRole(String userMstSeq, String roleSeq, String officeSeq);
    void removeUserRole(String userMstSeq, String roleSeq, String officeSeq);

    // ─── 플랜 관리 (슈퍼어드민) ───
    List<PlanVO> getAllPlans();
    void savePlan(PlanVO vo);
    void deletePlan(String planSeq);
    List<String> getPlanAllowedMenuSeqs(String planSeq);
    void savePlanMenus(String planSeq, List<String> menuSeqs, String createUser);
    List<OfficeVO> getOfficesByPlan(String planSeq);
    List<OfficeVO> getOfficesWithoutPlan();
    List<OfficeVO> getAllOfficesForAssign();
    void assignOfficePlan(String officeSeq, String planSeq);
    List<UserMasterVO> getUsersByPlan(String planSeq);
}
