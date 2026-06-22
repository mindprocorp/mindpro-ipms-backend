package kr.co.mindpro.ipms.domain.user.repository.db1;

import java.util.List;
import java.util.Optional;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.domain.user.vo.AppInventorVO;
import kr.co.mindpro.ipms.domain.user.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;

/**
 * 사용자 데이터 접근 인터페이스
 * 2026년 "MP_IPMS_PA" 통합 스키마 반영
 *
 * @author	 : intst
 * @fileName : UserMapper.java
 * @since	 : 2026. 01. 09. (수정)
 */
@Mapper
public interface UserMapper {
	
    /** 
     * 아이디를 기준으로 계정/상세/마스터 정보를 통합 조회합니다. 
     * (LoginInfoVO 대신 상세 정보가 포함된 UserMasterVO를 반환)
     */
    Optional<UserMasterVO> findByUserId(String userId);


    /** 등록된 모든 사용자 목록을 조회합니다. */
    List<UserMasterVO> findAllUsers();

    /** [멀티 사무소] 유저가 속한 활성 사무소 + 각 사무소에서의 admin_auth/role_nm/acct 조회. */
    List<kr.co.mindpro.ipms.domain.user.vo.MyOfficeVO> findMyOffices(@Param("userMstSeq") String userMstSeq);

    /**
     * [슈퍼관리자] 시스템 전체 사무소 목록.
     * 본인이 멤버인 사무소는 실제 role/admin_auth 표시,
     * 비멤버 사무소는 '관리자 모드'(SUPER_ADMIN/admin='Y')로 표시.
     */
    List<kr.co.mindpro.ipms.domain.user.vo.MyOfficeVO> findAllOfficesForSuperAdmin(@Param("userMstSeq") String userMstSeq);

    /** [슈퍼관리자 전환] 대상 사무소에서의 본인 멤버십 role_type (없으면 null). */
    String findRoleTypeByMembership(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [멀티 사무소] 유저가 ACTIVE membership을 해당 사무소에 가졌는지 (전환 전 체크). */
    int existsActiveMembership(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [멀티 사무소] 유저가 해당 사무소에 membership 존재 여부 (상태 무관) — join 전 중복 체크용. */
    int existsAnyMembership(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [탈퇴] 해당 사무소의 "나를 제외한" 다른 관리자(admin='Y') 수. */
    int countOtherAdmins(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [탈퇴] 해당 사무소의 "나를 제외한" 다른 활성 구성원 수. */
    int countOtherMembers(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [탈퇴] 유저가 가진 "해당 사무소를 제외한" 다른 활성 membership 수. */
    int countOtherMemberships(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [탈퇴] 유저의 다른 활성 사무소 1건 (자동 전환용, 최근 가입순). */
    String findFallbackOfficeSeq(@Param("userMstSeq") String userMstSeq, @Param("excludeOfficeSeq") String excludeOfficeSeq);

    /** [탈퇴] office_employee 소프트 삭제. */
    int leaveOfficeEmployee(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [승인] 호출자가 사무소 관리자(admin_auth='Y', ACTIVE)인지 검증. */
    int isOfficeAdmin(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [승인] target이 해당 사무소에 PENDING membership을 가졌는지. */
    int existsPendingMembership(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [승인] PENDING membership → ACTIVE 승격. */
    int approveOfficeMembership(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq, @Param("approverMstSeq") String approverMstSeq);

    /** [승인] user_info.acct_status_code가 해당 사무소에서 PENDING이면 ACTIVE로 전환 (로그인 차단 해제). */
    int unblockPendingUserInfo(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq, @Param("approverMstSeq") String approverMstSeq);

    /* ─── USRKR 생명주기 (하이브리드) ─── */

    /** 유저의 1인 사무소(USRKR) office_seq 조회. del_yn 무관. 없으면 null. */
    String findUserPersonalOffice(@Param("userMstSeq") String userMstSeq);

    /** USRKR office 소프트 비활성화. */
    int softDeletePersonalOffice(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** USRKR office_employee 소프트 비활성화. */
    int softDeletePersonalOfficeEmployee(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** USRKR office 재활성화 (del_yn 'Y' → 'N'). */
    int reactivatePersonalOffice(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** USRKR office_employee 재활성화 (del='N', acct='ACTIVE'). */
    int reactivatePersonalOfficeEmployee(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /* ─── 계정 탈퇴 ─── */

    /** 유저의 모든 활성 membership 소프트 삭제. */
    int deleteAllMemberships(@Param("userMstSeq") String userMstSeq);

    /** 유저 소유 USRKR office 모두 소프트 삭제 (계정 탈퇴 시). */
    int deleteAllPersonalOffices(@Param("userMstSeq") String userMstSeq);

    /** user_mst 소프트 삭제. */
    int softDeleteAccount(@Param("userMstSeq") String userMstSeq);

    /** user_info 소프트 삭제. */
    int softDeleteAccountUserInfo(@Param("userMstSeq") String userMstSeq);

    /** login_info 소프트 삭제. */
    int softDeleteAccountLoginInfo(@Param("userMstSeq") String userMstSeq);

    /** [사업자 전환] office_auth_yn 조회 (이미 사업자인지 사전 검증). */
    String findOfficeAuthYn(@Param("officeSeq") String officeSeq);

    /** [사업자 전환] 개인 사무소를 사업자 사무소로 업그레이드. */
    int upgradeOfficeToCorporate(UserMasterVO vo);

    /** [사업자 전환] user_mst.user_category_code 변경. */
    int updateUserCategoryCode(@Param("userMstSeq") String userMstSeq,
                                @Param("userCategoryCode") String userCategoryCode,
                                @Param("updateUser") String updateUser);

    /**
     * [사업자 전환] utb_user_role.user_role 갱신.
     *
     * @deprecated [LEGACY 유지보수만] utb_user_role 동기화 전용 (JWT priority).
     *   실제 권한은 oe.role_seq 기반. 추후 utb_user_role 제거 시 함께 삭제.
     */
    @Deprecated
    int updateUserRoleString(@Param("userMstSeq") String userMstSeq,
                              @Param("userRole") String userRole,
                              @Param("updateUser") String updateUser);

    /** [멀티 사무소] active office 전환 — user_info.office_seq 갱신. */
    int switchActiveOffice(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /** [멀티 사무소] active office 전환 — login_info.office_seq 갱신 (JWT 발급 시 이 값 사용). */
    int switchActiveOfficeLogin(@Param("userMstSeq") String userMstSeq, @Param("officeSeq") String officeSeq);

    /**
     * 사용자 마스터 일련번호를 기준으로 부여된 모든 권한 코드를 조회합니다.
     *
     * @deprecated [LEGACY 유지보수만] utb_user_role 테이블 — 실제 권한 결정에 거의 사용 안 됨.
     *   현재 JWT auth claim 발급 용도로만 쓰임 (Spring Security hasRole 체크). 추후 oe.role_seq → role_type 기반으로 통합 예정.
     */
    @Deprecated
    List<String> getUserRoleByMstSeq(@Param("userMstSeq") String userMstSeq);
    
    /** 
     * 사용자 마스터 정보를 업데이트합니다. 
     */
    int updateUserMst(UserMasterVO vo);

    /** 
     * 사용자 상세 정보를 업데이트합니다. 
     */
    int updateUserInfo(UserMasterVO vo);

    /** 
     * 비밀번호를 안전하게 수정합니다.
     */
    int updatePassword(@Param("userId") String userId, @Param("encPw") String encPw);

    /** 
     * 사용자 ID를 기준으로 계정 정보를 논리 삭제(del_yn = 'Y') 처리합니다. 
     */
    int deleteUserByUserId(String userId);    
    
    /* --- 통합 회원가입 (Multi-Table Insert) --- */
    
    /** 사무소 마스터 정보를 저장합니다. */
    int insertOfficeMst(UserMasterVO vo);

    /** [신규] 개인 1인 사무소 저장 (prefix=USRKR, office_auth_yn='N', plan_seq 필수) */
    int insertPersonalOffice(UserMasterVO vo);
    
    /** 사용자 마스터 정보를 저장합니다. */
    int insertUserMst(UserMasterVO vo);
    
    /** 사용자 상세 정보를 저장합니다. */
    int insertUserInfo(UserMasterVO vo);
    
    /** 로그인(인증) 정보를 저장합니다. */
    int insertLoginInfo(UserMasterVO vo);
    
    /** 사업자(비즈니스) 상세 정보를 저장합니다. */
    int insertBizInfo(UserMasterVO vo);
    
    /** 사무소 직원 정보를 저장합니다. */
    int insertOfficeEmployee(UserMasterVO vo);

    /** 사무소 직원 정보를 수정합니다. */
    int updateOfficeEmployee(UserMasterVO vo);

    /** 사무소 직원 정보를 논리 삭제합니다. (승인 거절 시) */
    int deleteOfficeEmployee(@Param("userMstSeq") String userMstSeq,
                             @Param("officeSeq") String officeSeq,
                             @Param("updateUser") String updateUser);

    /** 그룹코드+코드명으로 dtl_cd 조회 (대기/정상 등 코드 동적 조회용) */
    String findDtlCdByGrpCdAndName(@Param("grpCd") String grpCd,
                                    @Param("cdNm") String cdNm);

    /**
     * 사용자 권한 정보를 저장합니다. (ROLE_INDIV, ROLE_USER 등)
     *
     * @deprecated [LEGACY 유지보수만] utb_user_role 테이블 — JWT auth claim 동기화 용도로만 호출.
     *   실제 권한 분기는 utb_office_employee.admin_auth + utb_role_mst.role_type 기반. 추후 제거 예정.
     */
    @Deprecated
    int insertUserRole(@Param("userMstSeq") String userMstSeq,
                       @Param("userRole") String userRole,
                       @Param("createUser") String createUser);



    /**  사용자 상세 식별자(userInfoSeq)를 기준으로 사용자 성명(user_name_ko)만 조회합니다.
     * 관계자 리스트 출력 시 성명 매핑을 위한 용도입니다.
     */
    Optional<UserMasterVO> findUserMasterByInfoSeq(@Param("userInfoSeq") String userInfoSeq);

    /**
     * 이름/이메일/사무소로 통합 정보 조회
     */
    Optional<UserMasterVO> findUserMasterByNameAndEmail(
            @Param("userNameKo") String userNameKo,
            @Param("userEmail") String userEmail,
            @Param("officeSeq") String officeSeq);
    /** 검색용 (LIKE 검색)
     * 관계자 조회시 목록을 보여주기위한 용도입니다.
     */

    List<UserMasterVO> findUserInfoListByName(@Param("userNameKo") String userNameKo,
                                              @Param("officeSeq") String officeSeq);

    /** 승인 대기 직원 조회 (acct_status_code='PENDING') */
    List<UserMasterVO> findPendingUserInfoList(@Param("officeSeq") String officeSeq);

    /** acct_status_code만 단독 업데이트 (관리자 등록 시 ACTIVE 처리 등) */
    int updateAcctStatusCode(@Param("userMstSeq") String userMstSeq,
                             @Param("acctStatusCode") String acctStatusCode,
                             @Param("updateUser") String updateUser);

    int updateUserInfoToDeletedIfChanged(UserMasterVO vo);

    /** 이름 + 휴대폰으로 사용자 조회 (아이디 찾기) */
    Optional<UserMasterVO> findByNameAndPhone(@Param("userNameKo") String userNameKo,
                                              @Param("userMobileNo") String userMobileNo);

    /** 아이디 + 이름 + 휴대폰으로 사용자 조회 (비밀번호 찾기 본인 확인) */
    Optional<UserMasterVO> findByUserIdAndNameAndPhone(@Param("userId") String userId,
                                                       @Param("userNameKo") String userNameKo,
                                                       @Param("userMobileNo") String userMobileNo);

    /** 아이디 중복 확인 (존재 여부만) */
    boolean existsByUserId(@Param("userId") String userId);

    /** 로그인 실패 횟수 증가 */
    int incrementLoginFailCount(@Param("userId") String userId);

    /** 로그인 실패 횟수 초기화 */
    int resetLoginFailCount(@Param("userId") String userId);

    /** 계정 잠금 처리 */
    int lockAccount(@Param("userId") String userId);

    /** 계정 잠금 해제 (login_fail_count=0, login_lock_yn='N') — 비밀번호 초기화/관리자 해제용 */
    int unlockAccount(@Param("userId") String userId);

    /** 전화번호 중복 확인 */
    boolean existsByMobileNo(@Param("mobileNo") String mobileNo);

    /** userMstSeq로 사용자 검색 (소셜 연동 로그인용) */
    Optional<UserMasterVO> findByUserMstSeq(@Param("userMstSeq") String userMstSeq);


    // 목록 및 상세 조회
    List<UserInfoVO> selectInventorList(BaseSearchRequest request);
    int selectInventorListCount(BaseSearchRequest request);
    UserInfoVO selectInventorDetail(@Param("appInventorSeq") String appInventorSeq, @Param("officeSeq") String officeSeq);

    // 사용자 마스터 처리
    void insertInventorUserInfo(UserInfoVO vo);
    void updateInventorUserInfo(UserInfoVO vo);

    // 매핑 정보 처리
    int selectAppInventorMappingCount(AppInventorVO vo);
    void insertAppInventor(AppInventorVO vo);
    void updateAppInventor(AppInventorVO vo);

}
