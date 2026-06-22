package kr.co.mindpro.ipms.domain.system.repository.db1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.mindpro.ipms.domain.registry.vo.OfficeVO;
import kr.co.mindpro.ipms.domain.system.vo.MenuVO;
import kr.co.mindpro.ipms.domain.system.vo.PlanVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import java.util.List;

@Mapper
public interface SystemMapper {
    List<MenuVO> findAllMenus();
    boolean existsMenuCd(@Param("menuCd") String menuCd, @Param("excludeMenuSeq") String excludeMenuSeq);
    int countChildren(@Param("menuSeq") String menuSeq);
    void insertMenu(MenuVO vo);
    void updateMenu(MenuVO vo);
    void propagateSuperAdminOnly(@Param("rootMenuSeq") String rootMenuSeq, @Param("value") String value);

    // 플랜 관리 (슈퍼어드민)
    List<PlanVO> findAllPlans();
    String findPlanSeqByCd(@Param("planCd") String planCd);   // 플랜 코드로 seq 조회 (가입 시 기본 플랜 배정용)
    void insertPlan(PlanVO vo);
    void updatePlan(PlanVO vo);
    void deletePlan(@Param("planSeq") String planSeq);
    List<String> findPlanAllowedMenuSeqs(@Param("planSeq") String planSeq);
    List<String> findSuperAdminOnlyMenuSeqs();
    void deletePlanMenus(@Param("planSeq") String planSeq);
    void insertPlanMenu(@Param("planSeq") String planSeq, @Param("menuSeq") String menuSeq, @Param("createUser") String createUser);
    List<OfficeVO> findOfficesByPlan(@Param("planSeq") String planSeq);
    List<OfficeVO> findOfficesWithoutPlan();
    List<OfficeVO> findAllOfficesWithPlan();
    void assignOfficePlan(@Param("officeSeq") String officeSeq, @Param("planSeq") String planSeq);
    List<kr.co.mindpro.ipms.domain.user.vo.UserMasterVO> findUsersByPlan(@Param("planSeq") String planSeq);
    void updateMenuOrder(@Param("menuSeq") String menuSeq, @Param("dispOrd") int dispOrd);
    void updateOfficeCodeOrder(@Param("officeCodeSeq") String officeCodeSeq, @Param("sortOrd") int sortOrd);
    void updateDeptOrder(@Param("deptSeq") String deptSeq, @Param("sortOrd") int sortOrd);
    void updateFormTemplateOrder(@Param("formTemplateSeq") String formTemplateSeq, @Param("sortOrd") int sortOrd);
    void deleteMenu(String menuSeq);

    List<RoleVO> findAllRoles(@Param("officeSeq") String officeSeq);
    List<RoleVO> findActiveRoles(@Param("officeSeq") String officeSeq);
    RoleVO findRoleBySeq(@Param("roleSeq") String roleSeq);
    List<MenuVO> findAllActiveMenus();
    /** [Office] 사무소 플랜에 매핑된 메뉴만 (사무소 관리자가 RoleMngList에서 보는 트리). */
    List<MenuVO> findMenusByOffice(@Param("officeSeq") String officeSeq);
    void insertRole(RoleVO vo);
    void updateRole(RoleVO vo);
    void deleteRole(String roleSeq);

    List<RoleMenuMapVO> findRoleMenus(String roleSeq);
    List<RoleMenuMapVO> findAuthorizedMenuCodes(String roleSeq);
    List<RoleMenuMapVO> findAllMenusAsAdmin();
    List<RoleMenuMapVO> findAllMenusAsSuperAdmin();
    // 플랜 기반 런타임 메뉴 (사무소→플랜→메뉴)
    List<RoleMenuMapVO> findPlanAdminMenus(@Param("officeSeq") String officeSeq);
    List<RoleMenuMapVO> findPlanRoleMenus(@Param("roleSeq") String roleSeq, @Param("officeSeq") String officeSeq);
    void insertRoleMenu(RoleMenuMapVO vo);
    void deleteRoleMenus(String roleSeq);

    int findMaxRoleCdSeq(@Param("prefix") String prefix);

    List<kr.co.mindpro.ipms.domain.user.vo.UserMasterVO> findRoleUsers(@Param("roleSeq") String roleSeq, @Param("officeSeq") String officeSeq);
    void assignUserRole(@Param("userMstSeq") String userMstSeq, @Param("roleSeq") String roleSeq, @Param("officeSeq") String officeSeq);
    void removeUserRole(@Param("userMstSeq") String userMstSeq, @Param("roleSeq") String roleSeq, @Param("officeSeq") String officeSeq);
    int countRoleUsers(@Param("roleSeq") String roleSeq, @Param("officeSeq") String officeSeq);
    String findUserRoleSeq(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);
    String findDefaultRoleSeq(@Param("roleType") String roleType);
}
