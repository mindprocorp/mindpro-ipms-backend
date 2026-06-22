package kr.co.mindpro.ipms.domain.user.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.common.util.DataConvertUtil;
import kr.co.mindpro.ipms.common.util.SecurityUtil;
import kr.co.mindpro.ipms.domain.user.dto.response.UserInfoResponse;
import kr.co.mindpro.ipms.domain.user.vo.AppInventorVO;
import kr.co.mindpro.ipms.domain.user.vo.UserInfoVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mindpro.ipms.common.exception.BusinessException;
import kr.co.mindpro.ipms.common.exception.ErrorCode;
import kr.co.mindpro.ipms.common.notification.EmailService;
import kr.co.mindpro.ipms.domain.user.dto.request.UserRequest;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import kr.co.mindpro.ipms.domain.user.repository.db1.UserMapper;
import kr.co.mindpro.ipms.domain.user.service.UserService;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.common.enums.AcctStatus;
import kr.co.mindpro.ipms.domain.organization.service.OrganizationService;
import kr.co.mindpro.ipms.domain.paper.service.PaperService;
import kr.co.mindpro.ipms.domain.paper.vo.PaperRequestVO;
import kr.co.mindpro.ipms.domain.auth.repository.db1.SocialAuthMapper;
import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthVO;
import kr.co.mindpro.ipms.domain.auth.vo.SocialAuthMappVO;
import kr.co.mindpro.ipms.domain.system.repository.db1.SystemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 사용자 관리 비즈니스 로직 구현체
 *
 * @author   : intst
 * @fileName : UserServiceImpl.java
 * @since    : 2025. 12. 24.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 기본 설정 (성능 최적화)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final SystemMapper systemMapper;
    private final kr.co.mindpro.ipms.domain.registry.repository.db1.OfficeMapper officeMapper;
    private final PasswordEncoder passwordEncoder;
    private final SocialAuthMapper socialAuthMapper;
    private final OrganizationService organizationService;
    private final PaperService paperService;
    private final kr.co.mindpro.ipms.domain.paper.repository.db1.PaperMapper paperMapper;
    private final EmailService emailService;

    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 사용자 단건 조회: VO를 보안이 강화된 Response DTO로 변환하여 반환 */
    @Override
    public UserResponse getUser(String userId) {
        UserMasterVO vo = userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(vo);
    }
    /** 전체 사용자 조회: List<LoginInfoVO>를 List<UserResponse>로 스트림 변환 */
    @Override
    public List<UserResponse> getAllUsers() {
        // 전체 목록은 이름 등이 포함된 UserMasterVO로 가져오도록 Mapper 수정 반영
        List<UserMasterVO> userList = userMapper.findAllUsers();

        return userList.stream()
                .map(vo -> UserResponse.from(vo))
                .collect(Collectors.toList());
    }

    /** PENDING 유저 승인 — 관리자 권한 검증 후 oe + user_info 상태를 ACTIVE로 전환 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveEmployee(String approverMstSeq, String approverOfficeSeq, boolean isSuperAdmin,
                                 String targetUserMstSeq,
                                 kr.co.mindpro.ipms.domain.user.dto.request.ApproveEmployeeRequest request) {
        if (approverMstSeq == null || approverOfficeSeq == null || targetUserMstSeq == null
                || targetUserMstSeq.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        if (approverMstSeq.equals(targetUserMstSeq)) {
            throw new BusinessException("본인을 승인할 수 없습니다.", ErrorCode.ACCESS_DENIED);
        }
        if (request == null || request.roleSeq() == null || request.roleSeq().isBlank()) {
            throw new BusinessException("승인 시 역할(role_seq)은 필수입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 1. 권한 검증: 본인이 이 사무소의 관리자인가? (SUPER_ADMIN은 건너뛰기)
        if (!isSuperAdmin && userMapper.isOfficeAdmin(approverMstSeq, approverOfficeSeq) <= 0) {
            throw new BusinessException("사무소 관리자만 승인할 수 있습니다.", ErrorCode.ACCESS_DENIED);
        }

        // 2. 대상 검증: target이 이 사무소에 PENDING membership을 가졌는가?
        if (userMapper.existsPendingMembership(targetUserMstSeq, approverOfficeSeq) <= 0) {
            throw new BusinessException("승인 대기 중인 가입 신청을 찾을 수 없습니다.", ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 3. 역할 배정 (사무소별 per-membership)
        systemMapper.assignUserRole(targetUserMstSeq, request.roleSeq(), approverOfficeSeq);

        // 4. 직원 정보 업데이트 (직책/부서 등 — utb_office_employee)
        UserMasterVO empVo = new UserMasterVO();
        empVo.setUserMstSeq(targetUserMstSeq);
        empVo.setOfficeSeq(approverOfficeSeq);
        empVo.setOfficeEmployeePosition(request.officeEmployeePosition());
        empVo.setOfficeEmployeeDept(request.officeEmployeeDept());
        empVo.setDeptSeq(request.deptSeq());
        empVo.setWorkCode(request.workCode());
        empVo.setPositionCode(request.positionCode());
        empVo.setJobGradeCode(request.jobGradeCode());
        empVo.setUpdateUser(approverMstSeq);
        userMapper.updateOfficeEmployee(empVo);

        // 4-2. 사용자 상태 업데이트 (utb_user_info — 유형/근무/재직)
        //      입력된 항목만 갱신 (조건부 UPDATE — null 컬럼은 건드리지 않음)
        if (request.userTypeCode() != null || request.workStatusCode() != null || request.employStatusCode() != null) {
            UserMasterVO statusVo = new UserMasterVO();
            statusVo.setUserMstSeq(targetUserMstSeq);
            statusVo.setUserTypeCode(request.userTypeCode());
            statusVo.setWorkStatusCode(request.workStatusCode());
            statusVo.setEmployStatusCode(request.employStatusCode());
            statusVo.setUpdateUser(approverMstSeq);
            userMapper.updateUserInfo(statusVo);
        }

        // 5. oe.acct_status_code PENDING → ACTIVE
        userMapper.approveOfficeMembership(targetUserMstSeq, approverOfficeSeq, approverMstSeq);

        // 6. user_info의 home office가 이 사무소고 PENDING이었다면 ACTIVE로 (로그인 차단 해제)
        userMapper.unblockPendingUserInfo(targetUserMstSeq, approverOfficeSeq, approverMstSeq);

        // 7. [하이브리드] 승인 대상이 USRKR 단독 보유 중이었다면 → USRKR 자동 비활성
        //    이제 회사 소속이 되었으니 임시 거처 불필요. user_info.office_seq도 회사로 이동.
        String personalOfficeSeq = userMapper.findUserPersonalOffice(targetUserMstSeq);
        if (personalOfficeSeq != null && !personalOfficeSeq.equals(approverOfficeSeq)) {
            userMapper.softDeletePersonalOffice(targetUserMstSeq, personalOfficeSeq);
            userMapper.softDeletePersonalOfficeEmployee(targetUserMstSeq, personalOfficeSeq);
            // user_info/login_info의 active office를 승인 사무소로 이동 (USRKR 가리키던 것 방지)
            userMapper.switchActiveOffice(targetUserMstSeq, approverOfficeSeq);
            userMapper.switchActiveOfficeLogin(targetUserMstSeq, approverOfficeSeq);
            log.info("### [APPROVE] USRKR deactivated (office={}) for user={}", personalOfficeSeq, targetUserMstSeq);
        }
    }

    /** [사업자 전환] 개인 1인 사무소 → 사업자 사무소 업그레이드 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upgradeToCorporate(String userMstSeq, String officeSeq,
                                     UserRequest.CorpInfoRequest corpInfo,
                                     MultipartFile bizFile) {
        if (userMstSeq == null || officeSeq == null || corpInfo == null) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        if (bizFile == null || bizFile.isEmpty()) {
            throw new BusinessException("사업자등록증 파일은 필수입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 1. 본인이 해당 사무소의 관리자인지 + office_auth_yn='N' (개인) 인지 검증
        if (userMapper.isOfficeAdmin(userMstSeq, officeSeq) <= 0) {
            throw new BusinessException("본인 소유의 사무소에서만 사업자 전환 가능합니다.", ErrorCode.ACCESS_DENIED);
        }
        String authYn = userMapper.findOfficeAuthYn(officeSeq);
        if ("Y".equals(authYn)) {
            throw new BusinessException("이미 사업자로 전환된 사무소입니다.", ErrorCode.DUPLICATE_RESOURCE);
        }

        // 2. office_mst 업데이트 (회사명/주소/전화 + office_auth_yn='Y')
        UserMasterVO officeVo = new UserMasterVO();
        officeVo.setOfficeSeq(officeSeq);
        officeVo.setOfficeShortName(corpInfo.corpName());
        officeVo.setOfficeAddr(corpInfo.corpAddr());
        officeVo.setOfficeTel(corpInfo.corpTel());
        officeVo.setUpdateUser(userMstSeq);
        userMapper.upgradeOfficeToCorporate(officeVo);

        // 3. utb_biz_info INSERT
        UserMasterVO bizVo = new UserMasterVO();
        bizVo.setOfficeSeq(officeSeq);
        bizVo.setBizRegNo(corpInfo.corpRegNumber());
        bizVo.setCeoName(corpInfo.ceoName());
        bizVo.setBizCorpName(corpInfo.corpName());
        bizVo.setBizAddr(corpInfo.corpAddr());
        bizVo.setBizAddrDetail(corpInfo.corpAddrDetail());
        bizVo.setBizPostNo(corpInfo.corpZipCode());
        bizVo.setBizTelNo(corpInfo.corpTel());
        bizVo.setBizFaxNo(corpInfo.corpFax());
        bizVo.setBizType(corpInfo.corpType());
        bizVo.setCreateUser(userMstSeq);
        bizVo.setUpdateUser(userMstSeq);
        userMapper.insertBizInfo(bizVo);

        // 4. 사업자등록증 파일 업로드
        paperService.saveFileMapping(PaperRequestVO.builder()
                .officeSeq(officeSeq)
                .tblSeq(officeSeq)
                .tblCode("BIZ_INFO")
                .file(bizFile)
                .fileKindCode("90")
                .fileCategoryCode("bizLicenseFile")
                .docSeq("397")
                .createUser(userMstSeq)
                .build());

        // 5. user_mst.user_category_code = CORPORATE
        userMapper.updateUserCategoryCode(userMstSeq, "CORPORATE", userMstSeq);

        // 6. SYSTEM_ADMIN 역할로 업그레이드 (oe.role_seq = 진실의 원천)
        String adminRoleSeq = getSystemRoleSeq("SYSTEM_ADMIN");
        systemMapper.assignUserRole(userMstSeq, adminRoleSeq, officeSeq);
        // [LEGACY 유지] utb_user_role — JWT auth claim 동기화 전용. 추후 제거 예정.
        userMapper.updateUserRoleString(userMstSeq, "ROLE_USER", userMstSeq);

        log.info("### [UPGRADE TO CORPORATE] user={}, office={}, corp={}",
                userMstSeq, officeSeq, corpInfo.corpName());
    }

    /** [하이브리드] 유일 회사 사무소 탈퇴 + 1인 사무소로 복귀 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String leaveToPersonal(String userMstSeq, String officeSeq, String password) {
        if (userMstSeq == null || officeSeq == null || password == null || password.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        // 1. 비밀번호 검증
        UserMasterVO current = userMapper.findByUserMstSeq(userMstSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, current.getUserPassword())) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }
        // 2. 소속 확인
        if (userMapper.existsAnyMembership(userMstSeq, officeSeq) <= 0) {
            throw new BusinessException("해당 사무소에 소속되어 있지 않습니다.", ErrorCode.RESOURCE_NOT_FOUND);
        }
        // 3. 유일 관리자 + 다른 구성원 존재 차단 (데이터 무결성)
        int otherAdmins  = userMapper.countOtherAdmins(userMstSeq, officeSeq);
        int otherMembers = userMapper.countOtherMembers(userMstSeq, officeSeq);
        if (otherAdmins == 0 && otherMembers > 0) {
            throw new BusinessException("유일한 관리자이므로 탈퇴할 수 없습니다. 다른 관리자를 지정한 뒤 탈퇴해주세요.", ErrorCode.ACCESS_DENIED);
        }
        // 4. 대상 사무소 soft leave
        userMapper.leaveOfficeEmployee(userMstSeq, officeSeq);

        // 5. USRKR 복구/생성 + active office 이동
        restoreOrCreatePersonalOffice(userMstSeq);

        // 6. 최종 USRKR seq 조회 후 반환
        return userMapper.findUserPersonalOffice(userMstSeq);
    }

    /** [하이브리드] 계정 탈퇴 — 모든 소속 + 개인 사무소 + 계정 소프트 삭제 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeAccount(String userMstSeq, String password) {
        if (userMstSeq == null || password == null || password.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        // 1. 비번 검증
        UserMasterVO current = userMapper.findByUserMstSeq(userMstSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, current.getUserPassword())) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }
        // 2. 유일 관리자 + 다른 구성원 존재하는 회사 사무소가 있으면 차단
        //    (다른 유저에게 피해 주지 않도록)
        //    * 현재는 간단히 "모든 membership 일괄 삭제 후 본인 계정만" 진행.
        //    * 필요 시 추후 강화: findOfficesIAmSoleAdminOfWithMembers → 차단
        userMapper.deleteAllMemberships(userMstSeq);
        userMapper.deleteAllPersonalOffices(userMstSeq);
        userMapper.softDeleteAccount(userMstSeq);
        userMapper.softDeleteAccountUserInfo(userMstSeq);
        userMapper.softDeleteAccountLoginInfo(userMstSeq);
        log.info("### [CLOSE_ACCOUNT] user={} soft-deleted", userMstSeq);
    }

    /** 자진 탈퇴 (사무소별 방어 규칙 체크 후 soft delete) */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String leaveOffice(String userMstSeq, String officeSeq, String password) {
        if (userMstSeq == null || userMstSeq.isBlank()
                || officeSeq == null || officeSeq.isBlank()
                || password == null || password.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 1. 비밀번호 재확인 (탈퇴는 민감 행동)
        UserMasterVO current = userMapper.findByUserMstSeq(userMstSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, current.getUserPassword())) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        // 2. 소속 여부 확인 (PENDING도 포함 — 가입신청 취소도 허용)
        if (userMapper.existsAnyMembership(userMstSeq, officeSeq) <= 0) {
            throw new BusinessException("해당 사무소에 소속되어 있지 않습니다.", ErrorCode.RESOURCE_NOT_FOUND);
        }

        // 2. 유일 membership이면 차단 (계정 삭제로 유도)
        if (userMapper.countOtherMemberships(userMstSeq, officeSeq) <= 0) {
            throw new BusinessException("마지막 사무소는 탈퇴할 수 없습니다. 계정 삭제를 이용해주세요.", ErrorCode.ACCESS_DENIED);
        }

        // 3. 유일 관리자 + 다른 구성원 존재 시 차단
        int otherAdmins = userMapper.countOtherAdmins(userMstSeq, officeSeq);
        int otherMembers = userMapper.countOtherMembers(userMstSeq, officeSeq);
        if (otherAdmins == 0 && otherMembers > 0) {
            throw new BusinessException("유일한 관리자이므로 탈퇴할 수 없습니다. 다른 관리자를 지정한 뒤 탈퇴해주세요.", ErrorCode.ACCESS_DENIED);
        }

        // 4. soft delete
        userMapper.leaveOfficeEmployee(userMstSeq, officeSeq);

        // 5. fallback office seq 반환 (호출자가 현재 접속 사무소였으면 switchOffice 호출)
        return userMapper.findFallbackOfficeSeq(userMstSeq, officeSeq);
    }

    /** 초대코드로 다른 사무소에 추가 합류 (관리자 승인 대기) */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String joinOffice(String userMstSeq, String inviteCode) {
        if (userMstSeq == null || userMstSeq.isBlank()
                || inviteCode == null || inviteCode.isBlank()) {
            throw new BusinessException("잘못된 요청입니다.", ErrorCode.INVALID_INPUT_VALUE);
        }

        // 1. 초대코드로 사무소 조회
        kr.co.mindpro.ipms.domain.registry.vo.OfficeVO office = officeMapper.findByInviteCode(inviteCode)
                .orElseThrow(() -> new BusinessException("유효하지 않은 초대코드입니다.", ErrorCode.RESOURCE_NOT_FOUND));

        String targetOfficeSeq = office.getOfficeSeq();

        // 2. 이미 소속돼 있으면 중복 차단 (PENDING/ACTIVE 모두)
        if (userMapper.existsAnyMembership(userMstSeq, targetOfficeSeq) > 0) {
            throw new BusinessException("이미 소속되었거나 승인 대기 중인 사무소입니다.", ErrorCode.DUPLICATE_RESOURCE);
        }

        // 3. office_employee INSERT (PENDING, 관리자 승인 대기)
        UserMasterVO vo = new UserMasterVO();
        vo.setUserMstSeq(userMstSeq);
        vo.setOfficeSeq(targetOfficeSeq);
        vo.setAdminAuth("N");
        vo.setAcctStatusCode("PENDING");
        vo.setCreateUser(userMstSeq);
        vo.setUpdateUser(userMstSeq);
        userMapper.insertOfficeEmployee(vo);

        // 4. role_seq는 NULL — 관리자 승인 시 approveEmployee에서 명시 부여.
        //    "디폴트 일반사용자" 개념 제거: SYSTEM_ADMIN(시스템관리자)만 디폴트, 그 외는 모두 CUSTOM 명시 부여.

        return targetOfficeSeq;
    }

    /** 현재 유저가 속한 활성 사무소 목록 (멀티 사무소 드롭다운용). SUPER_ADMIN이면 전체 사무소. */
    @Override
    public List<kr.co.mindpro.ipms.domain.user.dto.response.MyOfficeResponse> getMyOffices(
            String userMstSeq, String currentOfficeSeq, boolean isSuperAdmin) {
        var sourceList = isSuperAdmin
                ? userMapper.findAllOfficesForSuperAdmin(userMstSeq)
                : userMapper.findMyOffices(userMstSeq);
        return sourceList.stream()
                .map(vo -> kr.co.mindpro.ipms.domain.user.dto.response.MyOfficeResponse.builder()
                        .officeSeq(vo.getOfficeSeq())
                        .officeShortName(vo.getOfficeShortName())
                        .officeAuthYn(vo.getOfficeAuthYn())
                        .adminAuth(vo.getAdminAuth())
                        .roleNm(vo.getRoleNm())
                        .roleType(vo.getRoleType())
                        .acctStatusCode(vo.getAcctStatusCode())
                        .isCurrent(vo.getOfficeSeq() != null
                                && vo.getOfficeSeq().equals(currentOfficeSeq))
                        .build())
                .collect(Collectors.toList());
    }

    /** 비밀번호 변경: 현재 비밀번호 검증 후 새 비밀번호 암호화 저장 + 계정 잠금/실패횟수 해제 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String userId, UserRequest.ChangePassword request) {
        // 내부 메서드 호출 대신 Mapper를 통해 직접 VO 조회 (보안 검증용)
        UserMasterVO user = userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 1. 현재 비밀번호 일치 여부 확인 (BCrypt 매칭)
        if (!passwordEncoder.matches(request.currentPw(), user.getUserPassword())) {
            throw new BusinessException(ErrorCode.AUTH_FAILED);
        }

        // 2. 새로운 비밀번호 해싱 후 업데이트
        userMapper.updatePassword(userId, passwordEncoder.encode(request.newPw()));

        // 3. 본인 인증 후 변경이므로 누적 실패횟수/잠금 상태 같이 해제 (UX 일관성)
        userMapper.unlockAccount(userId);
    }

    /** 사용자 정보 업데이트: 상세 정보 + 프로필 이미지 수정 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(String userId, UserRequest.Update request, MultipartFile profileImage) {
        UserMasterVO loginInfo = userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserMasterVO vo = new UserMasterVO();
        vo.setUserMstSeq(loginInfo.getUserMstSeq());
        vo.setUserInfoSeq(loginInfo.getUserInfoSeq());
        vo.setUserNameKo(request.userNameKo());
        vo.setUserNameEn(request.userNameEn());
        vo.setUserEmail(request.userEmail());
        vo.setUserMobileNo(request.userMobileNo());
        vo.setUserTelNo(request.userTelNo());
        vo.setUserFaxNo(request.userFaxNo());
        vo.setUserPostNo(request.userPostNo());
        vo.setUserAddr(request.userAddr());
        vo.setUserAddrDetail(request.userAddrDetail());
        vo.setDeptName(request.deptName());
        vo.setUserPosition(request.userPosition());
        vo.setUpdateUser(userId);

        // 프로필 이미지 처리 (공통 파일 시스템)
        if (profileImage != null && !profileImage.isEmpty()) {
            paperService.saveFileMapping(PaperRequestVO.builder()
                    .officeSeq(loginInfo.getOfficeSeq())
                    .tblSeq(loginInfo.getUserInfoSeq())
                    .tblCode("USER_PROFILE")
                    .file(profileImage)
                    .fileKindCode("90")
                    .fileCategoryCode("profileImage")
                    .docSeq("398")
                    .createUser(userId)
                    .build());
        }

        userMapper.updateUserInfo(vo);
    }

    /** 프로필 이미지 삭제: 공통 파일 매핑 soft delete */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProfileImage(String userId) {
        UserMasterVO loginInfo = userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 공통 파일 매핑 soft delete
        paperMapper.deleteByWork(PaperRequestVO.builder()
                .officeSeq(loginInfo.getOfficeSeq())
                .tblSeq(loginInfo.getUserInfoSeq())
                .fileKindCode("90")
                .docSeq("398")
                .updateUser(userId)
                .build());
    }

    /** 사용자 삭제: ID 기반 논리 삭제(del_yn = 'Y') */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String userId) {
        userMapper.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userMapper.deleteUserByUserId(userId);
    }

    /**
     * [신규] 통합 회원가입 구현:
     * MST, INFO, LOGIN 3개 테이블에 데이터를 분산 저장하며 트랜잭션을 관리합니다.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerIndividual(UserRequest.RegisterIndividual request) {
        // 1. 아이디(이메일) 중복 체크
        userMapper.findByUserId(request.userEmail()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.DUPLICATE_USER_ID);
        });

        // 1-1. 전화번호 중복 체크
        if (request.mobileNo() != null && userMapper.existsByMobileNo(request.mobileNo())) {
            throw new BusinessException("이미 가입된 전화번호입니다. 기존 아이디로 로그인해주세요.", ErrorCode.DUPLICATE_USER_ID);
        }

        // 2. 통합 VO 객체 생성 및 기본 필드 세팅
        UserMasterVO vo = new UserMasterVO();
        String currentAdmin = "SYSTEM";

        vo.setCreateUser(currentAdmin);
        vo.setUpdateUser(currentAdmin);
        vo.setDelYn("N");

        // [PK 및 FK 구성 요소 세팅]
        //  - 초대코드 있음 → 기존 사무소 합류 (PENDING, admin_auth='N')
        //  - 초대코드 없음 → 1인 사무소 자동 생성 (ACTIVE, admin_auth='Y', prefix USRKR)
        boolean joinedViaInvite = (request.officeId() != null && !request.officeId().isBlank());
        if (joinedViaInvite) {
            vo.setOfficeSeq(request.officeId());
            vo.setAcctStatusCode(resolveAcctStatusCode(AcctStatus.PENDING));
        } else {
            createPersonalOffice(vo, request.userName());   // vo.officeSeq 주입됨 (USRKR...)
            vo.setAcctStatusCode(resolveAcctStatusCode(AcctStatus.ACTIVE));
        }

        // [UTB_USER_MST 정보 세팅]
        vo.setUserCategoryCode(request.userCategoryCode());
        vo.setTermsAgree(request.termsAgree() ? "Y" : "N");
        vo.setPrivacypolicyAgree(request.privacyPolicyAgree() ? "Y" : "N");
        vo.setMarketingAgree(request.marketingAgree() ? "Y" : "N");
        vo.setMobileAuthYn("N");

        // 3. MST 저장 (MyBatis selectKey에 의해 vo.userMstSeq가 자동 생성/주입됨)
        userMapper.insertUserMst(vo);

        // 4. 상세 정보(UTB_USER_INFO) 및 로그인 정보(UTB_LOGIN_INFO) 필드 세팅
        // MST 저장 후 채워진 vo.getUserMstSeq()가 하위 테이블 인서트 시 사용됩니다.
        vo.setUserNameKo(request.userName());
        vo.setUserEmail(request.userEmail());
        vo.setUserMobileNo(request.mobileNo());
        vo.setUserAddr(request.userAddr());
        vo.setUserAddrDetail(request.userAddrDetail());
        vo.setUserPostNo(request.userZipCode());

        // [UTB_LOGIN_INFO] 정보 세팅
        vo.setUserId(request.userEmail());
        vo.setUserPassword(passwordEncoder.encode(request.userPassword()));
        vo.setLoginFailCount("0");
        vo.setEmailAuthYn("N");
        vo.setLoginLockYn("N");

        // 5. DB 저장 (MyBatis에서 insertUserInfo 호출 시 userInfoSeq가 자동 생성/주입됨)
        userMapper.insertUserInfo(vo);
        userMapper.insertLoginInfo(vo);

        // [LEGACY 유지] utb_user_role — JWT auth claim 동기화 전용. 실제 권한은 oe.role_seq + admin_auth.
        userMapper.insertUserRole(vo.getUserMstSeq(), "ROLE_INDIV", "SYSTEM");

        // 사무소 직원 등록
        if (joinedViaInvite) {
            // 초대 합류 — 관리자 승인 대기 (admin_auth='N')
            vo.setAdminAuth("N");
            userMapper.insertOfficeEmployee(vo);

            // role_seq는 NULL — approveEmployee에서 관리자가 명시 부여.
        } else {
            // 1인 사무소 소유자 — 본인이 자기 사무소 관리자 (role_seq=NULL, admin_auth='Y'로 충분)
            vo.setAdminAuth("Y");
            userMapper.insertOfficeEmployee(vo);
        }

        // 소셜 연동 (소셜 로그인으로 가입한 경우)
        saveSocialAuth(request.socialProvider(), request.socialProviderId(),
                request.socialEmail(), request.userName(), vo.getUserMstSeq());

        // [알림 메일] 개인 회원가입 성공 시 info@mindpro.co.kr 로 회원 정보 발송
        // 메일 발송 실패가 가입 트랜잭션 롤백으로 이어지지 않도록 try-catch 처리
        try {
            emailService.sendNewMemberNotification(
                    request.userName(),
                    request.userEmail(),
                    request.mobileNo(),
                    request.userCategoryCode(),
                    LocalDateTime.now().format(DATETIME_FMT)
            );
        } catch (Exception e) {
            log.warn("### [MAIL] 개인 회원가입 알림 발송 실패: email={}, error={}", request.userEmail(), e.getMessage());
        }
    }

    /** * [신규] 사업자 회원가입 구현:
     * MST, INFO, LOGIN, OFFICE_MST, BIZ_INFO 총 5개 테이블 연관 저장
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerCorporate(UserRequest.RegisterCorporate request, MultipartFile bizFile) {
        // 1. 중복 체크
        userMapper.findByUserId(request.userEmail()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.DUPLICATE_USER_ID);
        });

        // 1-1. 전화번호 중복 체크
        if (request.mobileNo() != null && userMapper.existsByMobileNo(request.mobileNo())) {
            throw new BusinessException("이미 가입된 전화번호입니다. 기존 아이디로 로그인해주세요.", ErrorCode.DUPLICATE_USER_ID);
        }

        UserMasterVO vo = new UserMasterVO();
        vo.setCreateUser("SYSTEM");
        vo.setUpdateUser("SYSTEM");

        // 2. [STEP 1] 사무소 저장 (기본 USER 플랜 자동 배정. 추후 관리자가 업그레이드)
        String userPlanSeq = systemMapper.findPlanSeqByCd("USER");
        if (userPlanSeq == null || userPlanSeq.isBlank()) {
            throw new BusinessException("기본 USER 플랜이 존재하지 않습니다. 시스템 관리자에게 문의하세요.",
                    ErrorCode.RESOURCE_NOT_FOUND);
        }
        vo.setOfficeShortName(request.corpInfo().corpName());
        vo.setOfficeAddr(request.corpInfo().corpAddr());
        vo.setOfficeTel(request.corpInfo().corpTel());
        vo.setOfficeInviteCode(generateInviteCode());
        vo.setPlanSeq(userPlanSeq);
        vo.setAcctStatusCode(resolveAcctStatusCode(AcctStatus.ACTIVE));
        userMapper.insertOfficeMst(vo);

        // 2-1. [STEP 1-1] 사무소 기본 데이터 초기화 (시드 코드 복사 + 루트 부서 생성)
        organizationService.initializeDefaultCodes(vo.getOfficeSeq(), request.corpInfo().corpName());

        // 3. [STEP 2] 사용자 마스터 저장 (앞서 생성된 officeSeq 사용, 내부 selectKey로 userMstSeq 채번)
        vo.setUserCategoryCode(request.userCategoryCode());
        vo.setTermsAgree(request.termsAgree() ? "Y" : "N");
        vo.setPrivacypolicyAgree(request.privacyPolicyAgree() ? "Y" : "N");
        vo.setMarketingAgree(request.marketingAgree() ? "Y" : "N");
        userMapper.insertUserMst(vo);

        // 4. [STEP 3] 사용자 상세 & 로그인 저장 (userInfoSeq 등 순차 채번)
        vo.setUserNameKo(request.userName());
        vo.setUserEmail(request.userEmail());
        vo.setUserMobileNo(request.mobileNo());
        vo.setUserAddr(request.userAddr());
        vo.setUserAddrDetail(request.userAddrDetail());
        vo.setUserPostNo(request.userZipCode());

        vo.setUserId(request.userEmail());
        vo.setUserPassword(passwordEncoder.encode(request.userPassword()));

        userMapper.insertUserInfo(vo);
        userMapper.insertLoginInfo(vo);

        // 5. [STEP 4] 사업자 정보 저장 (biz_reg_no 에 corpRegNumber 매핑)
        vo.setBizRegNo(request.corpInfo().corpRegNumber());
        vo.setCeoName(request.corpInfo().ceoName());
        vo.setBizCorpName(request.corpInfo().corpName());
        // 사업자등록증 파일 업로드 (공통 파일 시스템)
        if (bizFile != null && !bizFile.isEmpty()) {
            paperService.saveFileMapping(PaperRequestVO.builder()
                    .officeSeq(vo.getOfficeSeq())
                    .tblSeq(vo.getOfficeSeq())
                    .tblCode("BIZ_INFO")
                    .file(bizFile)
                    .fileKindCode("90")
                    .fileCategoryCode("bizLicenseFile")
                    .docSeq("397")
                    .createUser("SYSTEM")
                    .build());
        }
        vo.setBizAddr(request.corpInfo().corpAddr());
        vo.setBizAddrDetail(request.corpInfo().corpAddrDetail());
        vo.setBizPostNo(request.corpInfo().corpZipCode());
        vo.setBizTelNo(request.corpInfo().corpTel());
        vo.setBizFaxNo(request.corpInfo().corpFax());
        vo.setBizType(request.corpInfo().corpType());

        userMapper.insertBizInfo(vo);

        // [LEGACY 유지] utb_user_role — JWT auth claim 동기화 전용. 실제 권한은 oe.role_seq + admin_auth.
        userMapper.insertUserRole(vo.getUserMstSeq(), "ROLE_USER", "SYSTEM");

        // 사무소 직원 등록 (가입자 본인 = 사무소 관리자)
        vo.setAdminAuth("Y");
        userMapper.insertOfficeEmployee(vo);

        // SYSTEM_ADMIN 역할 자동 배정 (사업자 대표, per-membership).
        String adminRoleSeq = getSystemRoleSeq("SYSTEM_ADMIN");
        systemMapper.assignUserRole(vo.getUserMstSeq(), adminRoleSeq, vo.getOfficeSeq());

        // 소셜 연동 (소셜 로그인으로 가입한 경우)
        saveSocialAuth(request.socialProvider(), request.socialProviderId(),
                request.socialEmail(), request.userName(), vo.getUserMstSeq());

        log.info("### Registration Success: Office[{}] User[{}]", vo.getOfficeSeq(), request.userEmail());

        // [알림 메일] 사업자 회원가입 성공 시 info@mindpro.co.kr 로 회원 정보 발송
        // 메일 발송 실패가 가입 트랜잭션 롤백으로 이어지지 않도록 try-catch 처리
        try {
            String register = Objects.equals(request.corpInfo().corpType(), "1") ? "사업자(기업)" : "사업자(사무소)";

            emailService.sendNewCorporateNotification(
                    request.userName(),
                    request.userEmail(),
                    request.mobileNo(),
                    request.corpInfo().corpName(),
                    request.corpInfo().corpRegNumber(),
                    request.corpInfo().ceoName(),
                    register,
                    LocalDateTime.now().format(DATETIME_FMT)
            );
        } catch (Exception e) {
            log.warn("### [MAIL] 사업자 회원가입 알림 발송 실패: email={}, error={}", request.userEmail(), e.getMessage());
        }
    }

    private void saveSocialAuth(String provider, String providerId,
                                String socialEmail, String userName, String userMstSeq) {
        if (provider == null || providerId == null) return;

        SocialAuthVO authVO = new SocialAuthVO();
        authVO.setAuthSeq(providerId);
        authVO.setAuthApproach(provider.toUpperCase());
        authVO.setAuthToken("");
        authVO.setProviderEmail(socialEmail);
        authVO.setProviderName(userName);
        authVO.setCreateUser("SYSTEM");
        authVO.setUpdateUser("SYSTEM");
        socialAuthMapper.insertSocialAuth(authVO);

        SocialAuthMappVO mappVO = new SocialAuthMappVO();
        mappVO.setAuthSeq(providerId);
        mappVO.setAuthApproach(provider.toUpperCase());
        mappVO.setUserMstSeq(userMstSeq);
        mappVO.setCreateUser("SYSTEM");
        mappVO.setUpdateUser("SYSTEM");
        socialAuthMapper.insertSocialAuthMapp(mappVO);

        log.info("소셜 연동 저장: provider={}, userMstSeq={}", provider, userMstSeq);
    }

    /**
     * 관계자 등록검색간 필요
     */


    /**
     * 이름 기반 사용자정보 목록 검색 (LIKE 검색)
     */
    /**
     * 성명 LIKE 검색 및 DTO 변환
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUserInfoList(String userNameKo, String officeSeq) {
        // 1. Mapper를 통한 LIKE 검색 (기존에 정의한 findUserInfoListByName 사용)
        List<UserMasterVO> userList = userMapper.findUserInfoListByName(userNameKo, officeSeq);

        // 2. 제시해주신 UserResponse.from 메서드를 활용한 변환
        return userList.stream()
                .map(vo -> UserResponse.from(vo)) // 정적 메서드 활용
                .collect(Collectors.toList());
    }
    /**
     * 신규 인적 사항 등록 로직 구현
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerUserInfo(UserRequest.RegisterUserInfo request) {
        log.info("### [SERVICE] Registering new user info for: {}", request.userNameKo());

        // 1. DTO -> VO 변환
        UserMasterVO vo = new UserMasterVO();
        vo.setOfficeSeq(request.officeSeq());
        vo.setUserNameKo(request.userNameKo());
        vo.setUserEmail(request.userEmail());
        vo.setUserMobileNo(request.userMobileNo());
        vo.setUserAddr(request.userAddr());
        vo.setUserAddrDetail(request.userAddrDetail());
        vo.setUserPostNo(request.userPostNo());
        vo.setDeptName(request.deptName());
        vo.setUserPosition(request.userPosition());

        // 2. 작성자 및 수정자를 "SYSTEM"으로 강제 설정
        vo.setCreateUser("SYSTEM");
        vo.setUpdateUser("SYSTEM");

        // 2-1. 사용자 마스터 생성 (계정 없이 식별용)
        vo.setUserCategoryCode("EMPLOYEE");
        userMapper.insertUserMst(vo);

        // 관리자 직접 등록은 승인 절차 불필요 → 즉시 사용중
        vo.setAcctStatusCode(resolveAcctStatusCode(AcctStatus.ACTIVE));
        // (registerUserInfo는 관계자/직원 단순 등록용)

        // 3. Mapper 호출 (기존 insertUserInfo 활용)
        userMapper.insertUserInfo(vo);

        // 4. 사무소 직원 등록 + 사무소별 기본 역할 부여
        // [LEGACY 유지] utb_user_role — JWT auth claim 동기화 전용. 실제 권한은 oe.role_seq + admin_auth.
        userMapper.insertUserRole(vo.getUserMstSeq(), "ROLE_INDIV", "SYSTEM");
        if (request.officeSeq() != null && !request.officeSeq().isBlank()) {
            vo.setAdminAuth("N");
            vo.setOfficeEmployeePosition(request.userPosition());
            vo.setOfficeEmployeeDept(request.deptName());
            vo.setJobGradeCode(request.jobGradeCode());
            vo.setWorkCode(request.workCode());
            userMapper.insertOfficeEmployee(vo);

            // role_seq는 NULL — approveEmployee에서 관리자가 명시 부여.
        }

        log.debug("### [SERVICE] Successfully registered UserInfo with Seq: {}", vo.getUserInfoSeq());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployeeInfo(UserRequest.UpdateEmployee request, String officeSeq) {
        UserMasterVO vo = new UserMasterVO();
        vo.setUserMstSeq(request.userMstSeq());
        vo.setOfficeSeq(officeSeq);
        vo.setUserNameKo(request.userNameKo());
        vo.setUserEmail(request.userEmail());
        vo.setUserMobileNo(request.userMobileNo());
        vo.setUserAddr(request.userAddr());
        vo.setUserAddrDetail(request.userAddrDetail());
        vo.setUserPostNo(request.userPostNo());
        vo.setDeptName(request.deptName());
        vo.setUserPosition(request.userPosition());
        vo.setUserTypeCode(request.userTypeCode());
        vo.setWorkStatusCode(request.workStatusCode());
        vo.setEmployStatusCode(request.employStatusCode());
        vo.setAcctStatusCode(request.acctStatusCode());
        // [정합성] 역할은 oe.role_seq가 진실의 원천 — 여기서 legacy utb_user_info.role_seq 갱신 안 함.
        //         역할 변경은 systemService.assignUserRole 만 사용.
        vo.setUpdateUser(request.updateUser() != null ? request.updateUser() : "SYSTEM");

        // utb_user_info 수정
        userMapper.updateUserInfo(vo);

        // utb_office_employee 수정
        vo.setOfficeEmployeePosition(request.userPosition());
        vo.setOfficeEmployeeDept(request.deptName());
        vo.setPositionCode(request.positionCode());
        vo.setJobGradeCode(request.jobGradeCode());
        vo.setWorkCode(request.workCode());
        userMapper.updateOfficeEmployee(vo);

        log.info("### [SERVICE] Updated employee: userMstSeq={}", request.userMstSeq());
    }

    /**
     * 직원 승인 거절 처리.
     * - utb_office_employee 행 논리 삭제 (소속 해제 — 모든 직원 조회는 office_employee와 INNER JOIN하므로 자동 제외)
     * - utb_user_info.acct_status_code = NULL (재가입 시 다시 PENDING 받을 수 있도록 초기화)
     * → user_info의 office_seq는 변경하지 않음 (멤버십의 진실은 office_employee가 담당)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectEmployeeWithPassword(String adminMstSeq, String adminPw, String userMstSeq, String officeSeq) {
        if (adminPw == null || adminPw.isBlank()) {
            throw new BusinessException("관리자 비밀번호를 입력해주세요.", ErrorCode.INVALID_INPUT_VALUE);
        }
        if (adminMstSeq == null || adminMstSeq.equals(userMstSeq)) {
            throw new BusinessException("본인을 방출할 수 없습니다.", ErrorCode.ACCESS_DENIED);
        }
        // 1. 관리자 권한 검증
        if (userMapper.isOfficeAdmin(adminMstSeq, officeSeq) <= 0) {
            throw new BusinessException("사무소 관리자만 방출할 수 있습니다.", ErrorCode.ACCESS_DENIED);
        }
        // 2. 관리자 비밀번호 검증
        UserMasterVO admin = userMapper.findByUserMstSeq(adminMstSeq)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(adminPw, admin.getUserPassword())) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }
        // 3. 대상이 이 사무소에 소속돼 있어야 함
        if (userMapper.existsAnyMembership(userMstSeq, officeSeq) <= 0) {
            throw new BusinessException("해당 사무소에 소속된 직원이 아닙니다.", ErrorCode.RESOURCE_NOT_FOUND);
        }
        // 4. 실제 방출 (기존 rejectEmployee 위임)
        rejectEmployee(userMstSeq, officeSeq, adminMstSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectEmployee(String userMstSeq, String officeSeq, String updateUser) {
        String actor = (updateUser != null && !updateUser.isBlank()) ? updateUser : "SYSTEM";
        userMapper.deleteOfficeEmployee(userMstSeq, officeSeq, actor);
        userMapper.updateAcctStatusCode(userMstSeq, null, actor);

        // [하이브리드] 거절 후 유저가 활성 membership 0개면 USRKR 복구 또는 생성 → 유령 방지
        int remaining = userMapper.countOtherMemberships(userMstSeq, officeSeq);
        if (remaining == 0) {
            restoreOrCreatePersonalOffice(userMstSeq);
        }
        log.info("### [SERVICE] Rejected employee join: userMstSeq={}, officeSeq={}", userMstSeq, officeSeq);
    }

    /**
     * [하이브리드 헬퍼] USRKR 복구 or 신규 생성 + user_info/login_info.office_seq 이동.
     * - 기존 USRKR(del_yn='Y')이 있으면 활성화
     * - 없으면 새로 생성 (admin 생성 직원 계정처럼 USRKR 이력이 없는 유저용)
     */
    private void restoreOrCreatePersonalOffice(String userMstSeq) {
        String existingUsrkr = userMapper.findUserPersonalOffice(userMstSeq);
        String targetOfficeSeq;
        if (existingUsrkr != null) {
            userMapper.reactivatePersonalOffice(userMstSeq, existingUsrkr);
            userMapper.reactivatePersonalOfficeEmployee(userMstSeq, existingUsrkr);
            targetOfficeSeq = existingUsrkr;
        } else {
            // USRKR 이력 없음 (관리자 생성 직원 등) — 유저 이름으로 새 사무소 생성
            UserMasterVO info = userMapper.findByUserMstSeq(userMstSeq)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
            UserMasterVO vo = new UserMasterVO();
            vo.setUserMstSeq(userMstSeq);
            vo.setCreateUser(userMstSeq);
            vo.setUpdateUser(userMstSeq);
            createPersonalOffice(vo, info.getUserNameKo() != null ? info.getUserNameKo() : "개인");
            vo.setAdminAuth("Y");
            vo.setAcctStatusCode("ACTIVE");
            userMapper.insertOfficeEmployee(vo);
            targetOfficeSeq = vo.getOfficeSeq();
        }
        userMapper.switchActiveOffice(userMstSeq, targetOfficeSeq);
        userMapper.switchActiveOfficeLogin(userMstSeq, targetOfficeSeq);
        log.info("### [HYBRID] Personal office restored/created: user={}, office={}", userMstSeq, targetOfficeSeq);
    }

    /** 승인 대기 직원 목록 조회 (acct_status_code='PENDING'만) */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getPendingEmployeeList(String officeSeq) {
        List<UserMasterVO> userList = userMapper.findPendingUserInfoList(officeSeq);
        return userList.stream().map(UserResponse::from).collect(Collectors.toList());
    }

    /**
     * 관리자가 직접 직원 등록.
     * 기존 회원가입 로직(registerIndividual)을 재사용한 후 acct_status_code만 ACTIVE로 덮어쓰기.
     * → 코드 중복 없이 "관리자 등록은 승인 불필요" 정책을 보장.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerEmployeeByAdmin(kr.co.mindpro.ipms.domain.user.dto.request.AdminCreateEmployeeRequest request) {
        // 1. 가입 부분만 기존 RegisterIndividual로 위임 (계정 + 사무소 membership 생성)
        UserRequest.RegisterIndividual base = new UserRequest.RegisterIndividual(
                request.userCategoryCode(),
                request.userEmail(),
                request.userPassword(),
                request.userName(),
                request.mobileNo(),
                null, null, null,                       // userAddr / userAddrDetail / userZipCode
                request.termsAgree(),
                request.privacyPolicyAgree(),
                request.marketingAgree(),
                request.officeId(),
                null, null, null                        // socialProvider / socialProviderId / socialEmail
        );
        registerIndividual(base);

        // 2. 즉시 사용중 처리
        UserMasterVO created = userMapper.findByUserId(request.userEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // 2-1. utb_user_info.acct_status_code = ACTIVE
        userMapper.updateAcctStatusCode(created.getUserMstSeq(), resolveAcctStatusCode(AcctStatus.ACTIVE), "SYSTEM");
        // 2-2. utb_office_employee.acct_status_code PENDING → ACTIVE (펜딩 리스트는 이 컬럼 기준!)
        if (request.officeId() != null && !request.officeId().isBlank()) {
            userMapper.approveOfficeMembership(created.getUserMstSeq(), request.officeId(), "SYSTEM");
        }

        // 3. 후처리 — 입력된 항목만 적용
        String newUserMstSeq = created.getUserMstSeq();
        String officeSeq     = request.officeId();

        // 3-1. 역할 (oe.role_seq)
        if (officeSeq != null && !officeSeq.isBlank()
                && request.roleSeq() != null && !request.roleSeq().isBlank()) {
            systemMapper.assignUserRole(newUserMstSeq, request.roleSeq(), officeSeq);
        }

        // 3-2. 조직 정보 (utb_office_employee)
        if (officeSeq != null && !officeSeq.isBlank()) {
            UserMasterVO empVo = new UserMasterVO();
            empVo.setUserMstSeq(newUserMstSeq);
            empVo.setOfficeSeq(officeSeq);
            empVo.setOfficeEmployeePosition(request.officeEmployeePosition());
            empVo.setOfficeEmployeeDept(request.officeEmployeeDept());
            empVo.setPositionCode(request.positionCode());
            empVo.setJobGradeCode(request.jobGradeCode());
            empVo.setWorkCode(request.workCode());
            empVo.setUpdateUser("SYSTEM");
            userMapper.updateOfficeEmployee(empVo);
        }

        // 3-3. 상태 (utb_user_info — 유형/근무/재직)
        if (request.userTypeCode() != null || request.workStatusCode() != null || request.employStatusCode() != null) {
            UserMasterVO statusVo = new UserMasterVO();
            statusVo.setUserMstSeq(newUserMstSeq);
            statusVo.setUserTypeCode(request.userTypeCode());
            statusVo.setWorkStatusCode(request.workStatusCode());
            statusVo.setEmployStatusCode(request.employStatusCode());
            statusVo.setUpdateUser("SYSTEM");
            userMapper.updateUserInfo(statusVo);
        }

        log.info("### [SERVICE] Admin registered employee: userMstSeq={}", newUserMstSeq);
    }


    /**
     * ACCT_STATUS 코드값 동적 조회 (cd_nm 기반). DB에 없으면 enum dtl_cd로 fallback.
     * 매직 스트링 사용을 한 곳으로 격리하기 위한 헬퍼.
     */
    private String resolveAcctStatusCode(AcctStatus target) {
        String code = userMapper.findDtlCdByGrpCdAndName(AcctStatus.GROUP_CODE, target.getName());
        return (code == null || code.isBlank()) ? target.getCode() : code;
    }

    /**
     * 시스템 기본 역할(SUPER_ADMIN/SYSTEM_ADMIN/SYSTEM_USER)의 role_seq 조회.
     * 전역 역할은 DB 시드로 영구 보존되며, 누락된 경우 구성 오류이므로 예외 발생.
     */
    private String getSystemRoleSeq(String roleType) {
        String roleSeq = systemMapper.findDefaultRoleSeq(roleType);
        if (roleSeq == null || roleSeq.isBlank()) {
            throw new BusinessException(
                    "시스템 기본 역할이 누락되었습니다: " + roleType + ". DB 시드 확인 필요.",
                    ErrorCode.RESOURCE_NOT_FOUND);
        }
        return roleSeq;
    }

    /**
     * 개인 가입자를 위한 1인 사무소 자동 생성.
     * - office_seq: USRKR{YYYY}{7자리} (fn_get_dynamic_seq('utb_office_mst_user'))
     * - office_auth_yn: 'N'
     * - plan_seq: USER 플랜 기본값 (utb_plan_mst WHERE plan_cd='USER')
     * - office_short_name: 유저 이름 (차후 마이페이지에서 변경 가능)
     * - 결과로 vo.officeSeq / vo.officeShortName / vo.planSeq / vo.officeInviteCode 가 세팅됨
     */
    private void createPersonalOffice(UserMasterVO vo, String userName) {
        String userPlanSeq = systemMapper.findPlanSeqByCd("USER");
        if (userPlanSeq == null || userPlanSeq.isBlank()) {
            throw new BusinessException("기본 USER 플랜이 존재하지 않습니다. 시스템 관리자에게 문의하세요.",
                    ErrorCode.RESOURCE_NOT_FOUND);
        }
        vo.setOfficeShortName(userName);
        vo.setOfficeInviteCode(generateInviteCode());
        vo.setPlanSeq(userPlanSeq);
        userMapper.insertPersonalOffice(vo);   // selectKey로 vo.officeSeq 자동 주입 (USRKR...)
    }

    /** 랜덤 초대코드 생성 (8자리 영대문자+숫자) */
    private String generateInviteCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.security.SecureRandom();
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }




// 발명자 추가

    /**
     * 1. 고안자 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public BaseSearchResponse<UserInfoResponse.UserDetailResponse> getInventorList(BaseSearchRequest request) {
        request.setOfficeSeq(SecurityUtil.getOfficeSeq());
        request.setOffSet((request.getPage() - 1) * request.getPageSize());

        // DB에서 VO 리스트 조회
        List<UserInfoVO> dbList = userMapper.selectInventorList(request);
        int totalCount = userMapper.selectInventorListCount(request);

        // VO -> Response Record 빌드 변환
        List<UserInfoResponse.UserDetailResponse> dtoList = dbList.stream()
                .map(this::buildUserDetailResponse)
                .collect(Collectors.toList());

        return BaseSearchResponse.of(dtoList, request.getPage(), totalCount);
    }

    /**
     * 2. 고안자 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse.UserDetailResponse getInventorDetail(String appInventorSeq) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        UserInfoVO vo = userMapper.selectInventorDetail(appInventorSeq, officeSeq);
        return vo != null ? buildUserDetailResponse(vo) : null;
    }

    /**
     * 3. 고안자 단건 저장 (UserInfoSeq 와 AppInventorSeq 각각의 존재 여부에 따른 분기)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoResponse.UserDetailResponse saveInventor(UserRequest.UserDetailRequest data) {
        String officeSeq = SecurityUtil.getOfficeSeq();
        String loginUser = SecurityUtil.getUserInfoSeq();

        String userInfoSeq = data.userInfoSeq();      // 사용자 마스터 PK
        String appInventorSeq = data.appInventorSeq(); // 사건-발명자 매핑 PK

        // [STEP 1] 사용자 마스터(utb_user_info) 처리
        UserInfoVO mstVo = UserInfoVO.builder()
                .userInfoSeq(userInfoSeq)
                .officeSeq(officeSeq)
                .userNameKo(data.userNameKo())
                .userNameEn(data.userNameEn())
                .userNameZh(data.userNameZh())
                .userNameJa(data.userNameJa())
                .residentNo(data.residentNo())
                .userAddr(data.userAddr())
                .userAddrEn(data.userAddrEn())
                .userAddrZh(data.userAddrZh())
                .userAddrJa(data.userAddrJa())
                .countryCode(data.countryCode())
                .note(data.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        if (!StringUtils.hasText(userInfoSeq)) {
            // 사용자 키가 없으면 신규 생성 (selectKey로 userInfoSeq 채워짐)
            userMapper.insertInventorUserInfo(mstVo);
            userInfoSeq = mstVo.getUserInfoSeq();
        } else {
            // 사용자 키가 있으면 기존 정보 수정
            userMapper.updateInventorUserInfo(mstVo);
        }

        // [STEP 2] 사건-발명자 매핑(utb_app_inventor) 처리
        AppInventorVO mappingVo = AppInventorVO.builder()
                .appInventorSeq(appInventorSeq) // 화면에서 넘어온 매핑 PK
                .officeSeq(officeSeq)
                .tblSeq(data.tblSeq())
                .userInfoSeq(userInfoSeq)
                .sort(data.sort() != null ? DataConvertUtil.parseIntSafe(data.sort()) : 1)
                .note(data.note())
                .createUser(loginUser)
                .updateUser(loginUser)
                .delYn("N")
                .build();

        // 매핑 키(appInventorSeq) 존재 여부에 따라 Insert / Update 결정
        if (!StringUtils.hasText(appInventorSeq)) {
            // 매핑 키가 없으면 신규 연결
            userMapper.insertAppInventor(mappingVo);
        } else {
            // 매핑 키가 있으면 해당 행(Row) 업데이트
            userMapper.updateAppInventor(mappingVo);
        }

        // [STEP 3] 최종 상세 정보 반환
        return this.getInventorDetail(mappingVo.getAppInventorSeq());
    }

    /**
     * 4. 고안자 목록 일괄 저장
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseSearchResponse<UserInfoResponse.UserDetailResponse> saveInventorList(List<UserRequest.UserDetailRequest> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return BaseSearchResponse.of(List.of(), 1, 0);
        }

        for (UserRequest.UserDetailRequest request : dataList) {
            this.saveInventor(request);
        }

        // 갱신된 리스트 조회를 위한 요청 객체 구성 (첫 번째 데이터의 사건번호 기준)
        return this.getInventorList(BaseSearchRequest.builder()
                .tblSeq(dataList.get(0).tblSeq())
                .page(1)
                .pageSize(100)
                .build());
    }

    /**
     * VO 데이터를 Response Record(UserDetailResponse)로 조립
     */
    private UserInfoResponse.UserDetailResponse buildUserDetailResponse(UserInfoVO vo) {
        return UserInfoResponse.UserDetailResponse.builder()
                .userInfoSeq(vo.getUserInfoSeq())
                .appInventorSeq(vo.getAppInventorSeq())
                .sort(vo.getSort())
                .tblSeq(vo.getTblSeq())
                .userNameKo(vo.getUserNameKo())
                .userNameEn(vo.getUserNameEn())
                .userNameZh(vo.getUserNameZh())
                .userNameJa(vo.getUserNameJa())
                .residentNo(vo.getResidentNo())
                .countryCode(vo.getCountryCode())
                .userAddr(vo.getUserAddr())
                .userAddrEn(vo.getUserAddrEn())
                .userAddrZh(vo.getUserAddrZh())
                .userAddrJa(vo.getUserAddrJa())
                .note(vo.getNote())
                .build();
    }





}