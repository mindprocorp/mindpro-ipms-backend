package kr.co.mindpro.ipms.common.service;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.domain.system.repository.db1.SystemMapper;
import kr.co.mindpro.ipms.domain.system.service.SystemService;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import kr.co.mindpro.ipms.domain.system.vo.RoleVO;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final UserMapper userMapper;
    private final SystemService systemService;
    private final SystemMapper systemMapper;

    public UserResponse getUserInfo(UserDetails user) {
        String username = user.getUsername();

        UserMasterVO vo = userMapper.findByUserId(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        // 역할 메타 조회 (roleType 세팅용) — 프론트에서 SUPER_ADMIN 식별에 사용
        if (vo.getRoleSeq() != null && !vo.getRoleSeq().isBlank()) {
            RoleVO role = systemMapper.findRoleBySeq(vo.getRoleSeq());
            if (role != null) {
                vo.setRoleType(role.getRoleType());
                vo.setRoleName(role.getRoleNm());
            }
        }

        // [슈퍼관리자 전환 정책 보정]
        // 그 사무소의 oe(role_seq) 기반 권한이 1순위. 단,
        // - 본거지로 가서 oe.role_type=SUPER_ADMIN인 경우는 위 블록에서 자동 세팅됨.
        // - 비멤버 사무소(oe 없음)에 슈퍼관리자가 진입한 경우: 토큰 claim 기반으로 가상 권한 부여
        //   → CommonService Tier 매칭이 가능해져 메뉴가 정상 노출됨.
        if (vo.getRoleType() == null || vo.getRoleType().isBlank()) {
            boolean isSuperFromToken = user.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
            boolean isSysAdminFromToken = user.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_SYSTEM_ADMIN".equals(a.getAuthority()));
            if (isSuperFromToken) {
                vo.setRoleType("SUPER_ADMIN");
                vo.setAdminAuth("Y");
                vo.setRoleName("슈퍼관리자");
            } else if (isSysAdminFromToken) {
                vo.setRoleType("SYSTEM_ADMIN");
                vo.setAdminAuth("Y");
                vo.setRoleName("시스템관리자");
            }
        }

        List<RoleMenuMapVO> menus;
        // [Tier 1] SUPER_ADMIN — 전체 메뉴 (플랜 무시 + super_admin_only 포함)
        if ("SUPER_ADMIN".equals(vo.getRoleType())) {
            menus = systemMapper.findAllMenusAsSuperAdmin();
        }
        // [Tier 2] 사무소 관리자 — 자기 사무소 플랜에 허용된 메뉴 + 4권한 자동 Y
        //   판정 기준: oe.admin_auth='Y' (사무소별 진실의 원천)
        //   !!! role_type=SYSTEM_ADMIN 이라도 admin_auth='N' 이면 그 사무소에서 관리자 아님.
        //     글로벌 SYSTEM_ADMIN role이 다른 사무소 membership에 들러붙어도 메뉴 새지 않음.
        else if ("Y".equals(vo.getAdminAuth())) {
            menus = systemMapper.findPlanAdminMenus(vo.getOfficeSeq());
        }
        // [Tier 3] 일반 구성원 — 사무소 플랜 ∩ 역할 매핑 (CUSTOM 만 의미 있음)
        //   role_type=SYSTEM_ADMIN/SYSTEM_USER 인 글로벌 role이 oe.role_seq 에 잘못 박혀 있으면
        //   utb_role_menu_map 의 행이 멋대로 새서 메뉴가 다 나오는 사고가 생김 → 차단.
        else if (vo.getRoleSeq() != null && !vo.getRoleSeq().isBlank()
                && !"SYSTEM_ADMIN".equals(vo.getRoleType())
                && !"SYSTEM_USER".equals(vo.getRoleType())) {
            menus = systemMapper.findPlanRoleMenus(vo.getRoleSeq(), vo.getOfficeSeq());
        } else {
            menus = null;
        }

        return UserResponse.from(vo, menus);
    }
}
