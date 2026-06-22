package kr.co.mindpro.ipms.domain.user.service;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.user.dto.request.UserRequest;
import kr.co.mindpro.ipms.domain.user.dto.request.UserRequest.RegisterCorporate;
import kr.co.mindpro.ipms.domain.user.dto.response.UserInfoResponse;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * [Service Interface] 사용자 관리 서비스
 *
 * @author   : intst
 * @fileName : UserService.java
 * @since    : 2025. 12. 24.
 */
public interface UserService {

    /**
     * 특정 아이디를 가진 사용자의 상세 정보를 조회합니다.
     * 보안을 위해 비밀번호가 제외된 UserResponse를 반환합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자 정보 응답 객체 (UserResponse)
     */
    UserResponse getUser(String userId);

    /**
     * 등록된 전체 사용자 목록을 조회합니다.
     *
     * @return 전체 사용자 리스트 (UserResponse 목록)
     */
    List<UserResponse> getAllUsers();

    /**
     * 현재 로그인 유저가 속한 활성 사무소 목록 (멀티 사무소 드롭다운용).
     * SUPER_ADMIN인 경우 시스템 전체 사무소를 반환한다.
     *
     * @param userMstSeq      로그인 유저 PK
     * @param currentOfficeSeq 현재 JWT의 active 사무소 (isCurrent 표시용)
     * @param isSuperAdmin    SUPER_ADMIN 여부 (true면 utb_office_mst 전체)
     * @return 사무소 리스트
     */
    List<kr.co.mindpro.ipms.domain.user.dto.response.MyOfficeResponse> getMyOffices(
            String userMstSeq, String currentOfficeSeq, boolean isSuperAdmin);

    /**
     * [멀티 사무소] 로그인된 유저가 초대코드로 다른 사무소에 추가 합류.
     * office_employee INSERT (admin_auth='N', role_seq=사무소 기본 SYSTEM_USER, acct='PENDING').
     * 관리자 승인 필요. 이미 소속돼 있으면 중복 예외.
     *
     * @param userMstSeq 현재 로그인 유저 PK
     * @param inviteCode 대상 사무소 초대코드
     * @return 가입 신청된 사무소 seq
     */
    String joinOffice(String userMstSeq, String inviteCode);

    /**
     * [멀티 사무소] 자진 탈퇴.
     * - 유일 관리자 + 다른 구성원 존재 시 차단
     * - 유일 membership 시 차단 (계정 삭제 유도)
     * - 탈퇴 후 fallback 사무소 seq 반환 (현재 접속 중이었으면 자동 전환 대상)
     *
     * @return 탈퇴 후 fallback 사무소 seq (없으면 null — 이론상 도달 불가, 유일 membership 차단)
     */
    String leaveOffice(String userMstSeq, String officeSeq, String password);

    /**
     * [하이브리드] 유일 회사 사무소 탈퇴 시 "1인 사무소로 복귀" 선택 경로.
     * - 비번 검증 필수
     * - USRKR 복구/생성 후 user_info/login_info.office_seq 이동
     * - 대상 사무소(회사) 소프트 탈퇴
     * @return 복구/생성된 USRKR office_seq
     */
    String leaveToPersonal(String userMstSeq, String officeSeq, String password);

    /**
     * [하이브리드] 계정 탈퇴 — 완전한 계정 삭제.
     * 비번 검증 + 모든 oe/user_info/user_mst/login_info/USRKR 소프트 삭제.
     */
    void closeAccount(String userMstSeq, String password);

    /**
     * [개인 → 법인 전환] 1인 사무소 소유자가 사업자 인증 후 법인 유저로 전환.
     * - 현재 active 사무소가 USRKR(office_auth_yn='N')이고 본인이 admin이어야 함
     * - biz_info INSERT + office_mst 업데이트 (auth_yn='Y', 회사명 등) + user_mst.user_category_code='CORPORATE'
     * - 사업자등록증 파일 업로드 필수
     */
    void upgradeToCorporate(String userMstSeq, String officeSeq,
                             kr.co.mindpro.ipms.domain.user.dto.request.UserRequest.CorpInfoRequest corpInfo,
                             org.springframework.web.multipart.MultipartFile bizFile);

    /**
     * [승인] PENDING 상태의 target 유저를 현재 사무소에서 ACTIVE로 승격.
     * 호출자는 해당 사무소의 admin_auth='Y' 여야 함 (또는 SUPER_ADMIN).
     *
     * @param approverMstSeq 승인자 (현재 로그인) user_mst_seq
     * @param approverOfficeSeq 승인자 현재 사무소 (JWT에서)
     * @param isSuperAdmin SUPER_ADMIN 여부 (true면 사무소 관리자 체크 건너뛰기)
     * @param targetUserMstSeq 승인 대상 user_mst_seq
     */
    void approveEmployee(String approverMstSeq, String approverOfficeSeq, boolean isSuperAdmin,
                          String targetUserMstSeq,
                          kr.co.mindpro.ipms.domain.user.dto.request.ApproveEmployeeRequest request);

    /**
     * 사용자의 기본 정보(이름, 권한, 상태 등)를 수정합니다.
     *
     * @param userId  수정할 사용자 ID
     * @param request 수정할 데이터 정보
     */
    void updateUser(String userId, UserRequest.Update request, MultipartFile profileImage);

    void deleteProfileImage(String userId);

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId  비밀번호를 변경할 사용자 ID
     * @param request 현재 비밀번호와 새 비밀번호 정보
     */
    void changePassword(String userId, UserRequest.ChangePassword request);

    /**
     * 사용자를 시스템에서 삭제합니다.
     *
     * @param userId 삭제할 사용자 ID
     */
    void deleteUser(String userId);

    /**
     * [신규] 개인 회원가입 (통합 테이블 구조)
     * MST, INFO, LOGIN 테이블에 데이터를 분산 저장합니다.
     *
     * @param request 개인 회원가입 정보
     */
    void registerIndividual(UserRequest.RegisterIndividual request);

    /**
     * [신규] 사업자 회원가입
     * MST, INFO, LOGIN, OFFICE_MST, BIZ_INFO 총 5개 테이블에 연관 데이터를 저장합니다.
     *
     * @param request 사업자 회원가입 정보
     */
    void registerCorporate(UserRequest.RegisterCorporate request, MultipartFile bizFile);



    /**
     * 관계자 업무를 위한
     */


    /**
     * 성명(LIKE) 및 사무소 기준 사용자 상세 정보 검색
     * @param userNameKo 검색할 성명 키워드
     * @param officeSeq 사무소 일련번호
     * @return 검색된 사용자 리스트 (UserResponse)
     */
    List<UserResponse> searchUserInfoList(String userNameKo, String officeSeq);
    /**
     * 신규 인적 사항 단독 등록 (UTB_USER_INFO)
     * @param request 등록 요청 DTO
     */
    void registerUserInfo(UserRequest.RegisterUserInfo request);

    /** 직원 정보 수정 (UTB_USER_INFO + UTB_OFFICE_EMPLOYEE) */
    void updateEmployeeInfo(UserRequest.UpdateEmployee request, String officeSeq);

    /** 직원 승인 거절 (사무소 직원 매핑 삭제 + 사용자 정보 office_seq 리셋) */
    /**
     * 직원 방출 (승인 거절 포함) — 관리자 본인 비밀번호 확인 필수.
     *
     * @param adminMstSeq 방출 실행자(관리자) PK
     * @param adminPw     관리자 비밀번호 (검증용)
     * @param userMstSeq  방출 대상 직원 PK
     * @param officeSeq   대상 사무소
     */
    void rejectEmployeeWithPassword(String adminMstSeq, String adminPw, String userMstSeq, String officeSeq);

    /** @deprecated 비번 없이 방출 — 내부 전용. rejectEmployeeWithPassword 사용 권장. */
    void rejectEmployee(String userMstSeq, String officeSeq, String updateUser);

    /** 승인 대기 직원 목록 조회 */
    List<UserResponse> getPendingEmployeeList(String officeSeq);

    /** 관리자가 직접 직원 등록 (승인 절차 없이 즉시 사용중 + 역할/조직/상태 지정) */
    void registerEmployeeByAdmin(kr.co.mindpro.ipms.domain.user.dto.request.AdminCreateEmployeeRequest request);



    /** 고안자 목록 조회 */
    BaseSearchResponse<UserInfoResponse.UserDetailResponse> getInventorList(BaseSearchRequest request);

    /** 고안자 상세 조회 */
    UserInfoResponse.UserDetailResponse getInventorDetail(String appInventorSeq);

    /** 고안자 단건 저장 */
    UserInfoResponse.UserDetailResponse saveInventor(UserRequest.UserDetailRequest data);

    /** 고안자 목록 일괄 저장 (갱신된 목록 반환) */
    BaseSearchResponse<UserInfoResponse.UserDetailResponse> saveInventorList(List<UserRequest.UserDetailRequest> dataList);

}