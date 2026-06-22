package kr.co.mindpro.ipms.domain.user.controller;

import java.util.List;

import kr.co.mindpro.ipms.common.dto.request.BaseSearchRequest;
import kr.co.mindpro.ipms.common.dto.response.BaseSearchResponse;
import kr.co.mindpro.ipms.domain.user.dto.response.UserInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import kr.co.mindpro.ipms.security.vo.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.mindpro.ipms.common.dto.response.ApiResponse;
import kr.co.mindpro.ipms.domain.user.dto.request.UserRequest;
import kr.co.mindpro.ipms.domain.user.dto.response.UserResponse;
import kr.co.mindpro.ipms.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [Controller] 사용자 관리 API
 * 사용자 생성, 조회, 수정, 삭제 및 비밀번호 변경 기능을 제공.
 * 모든 요청 경로는 /api/users 로 시작
 *
 * @author	 : intst
 * @fileName : UserController.java
 * @since	 : 2025. 12. 24.
 */
@Slf4j
@Tag(name = "User API", description = "사용자 CRUD 및 비밀번호 변경 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final kr.co.mindpro.ipms.domain.auth.service.AuthService authService;

    /**
     * 전체 사용자 목록 조회 API
     * GET /api/users
     */
    @Operation(summary = "전체 사용자 목록 조회", description = "등록된 모든 사용자의 리스트를 반환합니다. (비밀번호 제외)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "전체 사용자 목록 조회 성공", users));
    }

    /**
     * 특정 사용자 상세 조회 API
     * GET /api/users/{userId}
     */
    @Operation(summary = "특정 사용자 상세 조회", description = "ID를 기준으로 한 명의 사용자 정보를 조회합니다. (비밀번호 제외)")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
        @Parameter(description = "조회할 사용자 아이디", example = "admin@exmail.com") 
        @PathVariable("userId") String userId
    ) {
    		
    	 	// 이 로그가 콘솔에 어떻게 찍히는지 확인하는 것이 최우선입니다.
        log.debug("### [DEBUG] Path userId: {}", userId);
                
//        UserResponse user = userService.getUser(userId);
        UserResponse user = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 상세 조회 성공", user));
    }

    /**
     * 사용자 기본 정보 수정 API
     * PUT /api/users/{userId}
     */
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 및 프로필 이미지를 업데이트합니다.")
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    public ResponseEntity<ApiResponse<Void>> updateUser(
        @Parameter(description = "수정할 사용자 아이디") @PathVariable("userId") String userId,
        @RequestPart("data") UserRequest.Update request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.updateUser(userId, request, profileImage);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 정보가 성공적으로 수정되었습니다."));
    }

    /**
     * 프로필 이미지 삭제 API
     * DELETE /api/users/{userId}/profile-image
     */
    @Operation(summary = "프로필 이미지 삭제", description = "S3에서 프로필 이미지를 삭제하고 DB URL을 초기화합니다.")
    @DeleteMapping("/{userId}/profile-image")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.name")
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage(
        @Parameter(description = "대상 사용자 아이디") @PathVariable("userId") String userId) {
        userService.deleteProfileImage(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로필 이미지가 삭제되었습니다."));
    }

    /**
     * 비밀번호 변경 API
     * PATCH /api/users/{userId}/password
     */
    @Operation(summary = "비밀번호 변경", description = "기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.")
    @PatchMapping("/{userId}/password")
    @PreAuthorize("#userId == authentication.name")
    public ResponseEntity<ApiResponse<Void>> changePassword(
        @Parameter(description = "대상 사용자 아이디") @PathVariable("userId") String userId,
        @Valid @RequestBody UserRequest.ChangePassword request) {
        userService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "비밀번호가 성공적으로 변경되었습니다."));
    }

    /**
     * 사용자 삭제 API (Soft Delete)
     * DELETE /api/users/{userId}
     */
    @Operation(summary = "사용자 삭제", description = "아이디를 기준으로 계정을 삭제(del_yn='Y') 처리합니다.")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@Parameter(description = "삭제할 사용자 아이디") @PathVariable("userId") String userId) {
        // 서비스의 변경된 메서드명 반영
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 계정이 삭제 처리되었습니다."));
    }
    
    /**
     * 신규 개인 회원가입 API
     */
    @Operation(summary = "신규 개인 회원가입", description = "개인 사용자의 정보를 포함하여 회원가입을 진행합니다.")
    @PostMapping("/register/individual")
    public ResponseEntity<ApiResponse<Void>> registerIndividual(@Valid @RequestBody UserRequest.RegisterIndividual request) {
        log.info("### [REGISTER INDIVIDUAL] New individual registration: {}", request.userEmail());
        
        userService.registerIndividual(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "개인 회원가입이 완료되었습니다."));
    }
    
    /**
     * 신규 사업자 회원가입 API
     * POST /api/users/register/corporate
     */
    @Operation(summary = "신규 사업자 회원가입",
               description = "개인 정보와 사업자 상세 정보를 포함하여 회원가입을 진행합니다.")
    @PostMapping(value = "/register/corporate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> registerCorporate(
            @Valid @RequestPart("data") UserRequest.RegisterCorporate request,
            @RequestPart(value = "bizFile", required = false) MultipartFile bizFile) {
        log.info("### [REGISTER CORPORATE] New corporate registration: {} / {}",
                 request.userEmail(), request.corpInfo().corpName());

        userService.registerCorporate(request, bizFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "사업자 회원가입이 완료되었습니다."));
    }


    /**
     * 관계자 에서  사용
     *
     */



    /**
     * 신규 인적 사항 등록 API
     * POST /api/users/register/userinfo
     */
    @Operation(summary = "신규 인적 사항 등록", description = " 인적 정보만 등록합니다.")
    @PostMapping("/register/userinfo")
    public ResponseEntity<ApiResponse<Void>> registerUserInfo(@Valid @RequestBody UserRequest.RegisterUserInfo request) {
        log.info("### [REGISTER USERINFO] New entry for: {}", request.userNameKo());

        userService.registerUserInfo(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "인적 사항이 성공적으로 등록되었습니다."));
    }
    @Operation(summary = "직원 정보 수정", description = "직원 상세 정보 및 사무소 직원 정보를 수정합니다.")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/employee")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody UserRequest.UpdateEmployee request) {
        log.info("### [UPDATE EMPLOYEE] userMstSeq: {}", request.userMstSeq());
        userService.updateEmployeeInfo(request, user.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "직원 정보가 수정되었습니다."));
    }

    @Operation(summary = "직원 방출 (승인 거절 포함)", description = "관리자 본인 비밀번호 확인 후 직원을 사무소에서 방출합니다. 유령 방지를 위해 방출 후 대상 유저의 1인 사무소가 자동 복구됩니다.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/employee/{userMstSeq}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectEmployee(
            @AuthenticationPrincipal CustomUserDetails admin,
            @PathVariable("userMstSeq") String userMstSeq,
            @RequestBody java.util.Map<String, String> body) {
        String password = body.get("password");
        log.info("### [REJECT EMPLOYEE] admin={}, target={}, office={}", admin.getUsername(), userMstSeq, admin.getOfficeSeq());
        userService.rejectEmployeeWithPassword(admin.getUserMstSeq(), password, userMstSeq, admin.getOfficeSeq());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "직원이 방출되었습니다."));
    }

    @Operation(summary = "내 소속 사무소 목록", description = "로그인 유저가 속한 활성 사무소 + 각 사무소에서의 admin_auth/role/상태 리스트 (전환 드롭다운용). SUPER_ADMIN은 전체 사무소.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-offices")
    public ResponseEntity<ApiResponse<List<kr.co.mindpro.ipms.domain.user.dto.response.MyOfficeResponse>>> getMyOffices(
            @AuthenticationPrincipal CustomUserDetails user) {
        boolean isSuper = user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
        var offices = userService.getMyOffices(user.getUserMstSeq(), user.getOfficeSeq(), isSuper);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "내 소속 사무소 조회 성공", offices));
    }

    @Operation(summary = "사무소 전환", description = "active office를 전환하고 새 JWT를 재발급합니다. 일반 사용자는 active membership 필요. SUPER_ADMIN은 임의 사무소로 전환 가능.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/switch-office/{officeSeq}")
    public ResponseEntity<ApiResponse<kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse>> switchOffice(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("officeSeq") String officeSeq) {
        boolean isSuper = user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
        log.info("### [SWITCH OFFICE] user={}, from={} to={}, isSuper={}", user.getUsername(), user.getOfficeSeq(), officeSeq, isSuper);
        var res = authService.switchOffice(user.getUsername(), user.getUserMstSeq(), officeSeq, isSuper);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사무소 전환 완료", res));
    }

    @Operation(summary = "초대코드로 사무소 합류", description = "로그인된 유저가 초대코드로 다른 사무소에 추가 합류합니다 (관리자 승인 대기 상태).")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/join-office")
    public ResponseEntity<ApiResponse<String>> joinOffice(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody java.util.Map<String, String> body) {
        String inviteCode = body.get("inviteCode");
        log.info("### [JOIN OFFICE] user={}, inviteCode={}", user.getUsername(), inviteCode);
        String officeSeq = userService.joinOffice(user.getUserMstSeq(), inviteCode);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사무소 가입 신청 완료 (관리자 승인 대기)", officeSeq));
    }

    @Operation(summary = "PENDING 직원 승인", description = "현재 사무소의 관리자가 PENDING 직원을 역할/직원정보와 함께 ACTIVE로 승격합니다.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/approve/{userMstSeq}")
    public ResponseEntity<ApiResponse<Void>> approveEmployee(
            @AuthenticationPrincipal CustomUserDetails approver,
            @PathVariable("userMstSeq") String targetUserMstSeq,
            @Valid @RequestBody kr.co.mindpro.ipms.domain.user.dto.request.ApproveEmployeeRequest body) {
        log.info("### [APPROVE] approver={}, target={}, office={}, roleSeq={}",
                approver.getUsername(), targetUserMstSeq, approver.getOfficeSeq(), body.roleSeq());
        boolean isSuper = approver.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
        userService.approveEmployee(approver.getUserMstSeq(), approver.getOfficeSeq(), isSuper, targetUserMstSeq, body);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "직원 승인이 완료되었습니다."));
    }

    @Operation(summary = "[개인 → 법인 전환]", description = "1인 사무소를 사업자 사무소로 전환. 사업자등록증 파일 필수. office_auth_yn='Y' + biz_info 추가 + user_category_code=CORPORATE.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/upgrade-to-corporate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse>> upgradeToCorporate(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestPart("corpInfo") UserRequest.CorpInfoRequest corpInfo,
            @RequestPart("bizFile") MultipartFile bizFile) {
        log.info("### [UPGRADE] user={}, office={}, corp={}", user.getUsername(), user.getOfficeSeq(), corpInfo.corpName());
        userService.upgradeToCorporate(user.getUserMstSeq(), user.getOfficeSeq(), corpInfo, bizFile);
        // 전환 후 JWT 재발급 (role/category 변경 반영) — 본인 사무소 전환이므로 isSuper=false 무관
        boolean isSuper = user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
        var res = authService.switchOffice(user.getUsername(), user.getUserMstSeq(), user.getOfficeSeq(), isSuper);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사업자 전환이 완료되었습니다.", res));
    }

    @Operation(summary = "[하이브리드] 유일 회사 탈퇴 + 1인 사무소 복귀", description = "비밀번호 재확인 후 해당 사무소 탈퇴 + 1인 사무소 복구/생성 + JWT 재발급.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/leave-to-personal/{officeSeq}")
    public ResponseEntity<ApiResponse<kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse>> leaveToPersonal(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("officeSeq") String officeSeq,
            @RequestBody java.util.Map<String, String> body) {
        String password = body.get("password");
        log.info("### [LEAVE TO PERSONAL] user={}, officeSeq={}", user.getUsername(), officeSeq);
        String newOfficeSeq = userService.leaveToPersonal(user.getUserMstSeq(), officeSeq, password);
        // JWT 재발급 (active office = USRKR) — 본인 소유 USRKR로 전환이라 SUPER_ADMIN 분기 불필요
        var res = authService.switchOffice(user.getUsername(), user.getUserMstSeq(), newOfficeSeq, false);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "1인 사무소로 전환되었습니다.", res));
    }

    @Operation(summary = "[하이브리드] 계정 탈퇴", description = "비밀번호 확인 후 모든 소속 + 개인 사무소 + 계정 완전 소프트 삭제.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/close-account")
    public ResponseEntity<ApiResponse<Void>> closeAccount(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody java.util.Map<String, String> body) {
        String password = body.get("password");
        log.info("### [CLOSE ACCOUNT] user={}", user.getUsername());
        userService.closeAccount(user.getUserMstSeq(), password);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "계정이 탈퇴 처리되었습니다."));
    }

    @Operation(summary = "사무소 자진 탈퇴", description = "본인이 소속된 사무소에서 탈퇴합니다. 비밀번호 재확인 필수. 현재 접속 중인 사무소라면 다른 사무소로 자동 전환 + JWT 재발급.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/leave-office/{officeSeq}")
    public ResponseEntity<ApiResponse<kr.co.mindpro.ipms.domain.auth.dto.response.LoginResponse>> leaveOffice(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("officeSeq") String officeSeq,
            @RequestBody java.util.Map<String, String> body) {
        String password = body.get("password");
        log.info("### [LEAVE OFFICE] user={}, officeSeq={}", user.getUsername(), officeSeq);

        String fallbackOfficeSeq = userService.leaveOffice(user.getUserMstSeq(), officeSeq, password);

        // 현재 접속 중이던 사무소 탈퇴 → fallback으로 전환 + 새 JWT
        if (officeSeq.equals(user.getOfficeSeq()) && fallbackOfficeSeq != null) {
            boolean isSuper = user.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_SUPER_ADMIN".equals(a.getAuthority()));
            var res = authService.switchOffice(user.getUsername(), user.getUserMstSeq(), fallbackOfficeSeq, isSuper);
            return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                    "사무소에서 탈퇴했으며 다른 사무소로 전환되었습니다.", res));
        }

        // 다른 사무소 탈퇴 → JWT 유지 (null 응답)
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사무소에서 탈퇴했습니다.", null));
    }

    @Operation(summary = "승인 대기 직원 조회", description = "초대코드로 가입한 후 승인 대기 중인 직원 목록을 조회합니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending-employees")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getPendingEmployees(
            @AuthenticationPrincipal CustomUserDetails user) {
        String officeSeq = user.getOfficeSeq();
        log.info("### [PENDING EMPLOYEES] officeSeq: {}", officeSeq);
        List<UserResponse> results = userService.getPendingEmployeeList(officeSeq);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "승인 대기 직원 조회 성공", results));
    }

    @Operation(summary = "관리자 직접 직원 등록", description = "관리자가 직접 직원을 등록합니다. 승인 절차 없이 즉시 사용중 상태 + 입력한 역할/조직/상태로 생성됩니다.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/admin/employee")
    public ResponseEntity<ApiResponse<Void>> registerEmployeeByAdmin(
            @AuthenticationPrincipal CustomUserDetails admin,
            @Valid @RequestBody kr.co.mindpro.ipms.domain.user.dto.request.AdminCreateEmployeeRequest request) {
        log.info("### [ADMIN REGISTER EMPLOYEE] admin: {}, email: {}", admin.getUsername(), request.userEmail());
        userService.registerEmployeeByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "직원이 등록되었습니다."));
    }

    /**
     * 성명 기반 사용자 검색 API (LIKE 검색)     *
     */
    @Operation(summary = "성명 기반 사용자 검색", description = "성명을 키워드로 포함하는 사용자 상세 정보를 검색합니다.")
    @GetMapping("/search/userinfo/list")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUserInfo(
            @Parameter(description = "검색할 성명 키워드") @RequestParam("userNameKo") String userNameKo,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        String officeSeq = user.getOfficeSeq();
        log.info("### [SEARCH USER] Name: {}, Office: {}", userNameKo, officeSeq);

        List<UserResponse> results = userService.searchUserInfoList(userNameKo, officeSeq);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 검색 성공", results));
    }



    // 담당자

    @Operation(summary = "고안자 목록 조회")
    @PostMapping("/inventors/list")
    public ResponseEntity<ApiResponse<BaseSearchResponse<UserInfoResponse.UserDetailResponse>>> getInventorList(
            @Valid @RequestBody BaseSearchRequest request) {
        // 목록 조회 시에도 상세 정보 규격(UserDetailResponse)을 사용하여 반환
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고안자 목록 조회 성공",
                userService.getInventorList(request)));
    }

    @Operation(summary = "고안자 단건 상세 조회")
    @GetMapping("/inventor/detail/{userInfoSeq}")
    public ResponseEntity<ApiResponse<UserInfoResponse.UserDetailResponse>> getInventorDetail(
            @Parameter(description = "발명자키")  @RequestParam("appInventorSeq") String appInventorSeq
            ) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고안자 상세 조회 성공",
                userService.getInventorDetail(appInventorSeq)));
    }

    @Operation(summary = "고안자 단건 저장")
    @PostMapping("/inventor")
    public ResponseEntity<ApiResponse<UserInfoResponse.UserDetailResponse>> saveInventor(
            @Valid @RequestBody UserRequest.UserDetailRequest data) {
        // 저장 후 상세 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "고안자 정보 저장 성공",
                        userService.saveInventor(data)));
    }

    @Operation(summary = "고안자 목록 일괄 저장")
    @PostMapping("/inventors/bulk-save")
    public ResponseEntity<ApiResponse<BaseSearchResponse<UserInfoResponse.UserDetailResponse>>> saveInventorList(
            @Valid @RequestBody List<UserRequest.UserDetailRequest> dataList) {

        // 일괄 저장 후 갱신된 전체 목록을 상세 정보 규격 리스트로 반환
        BaseSearchResponse<UserInfoResponse.UserDetailResponse> updatedResponse = userService.saveInventorList(dataList);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "고안자 목록 일괄 저장 성공",
                updatedResponse));
    }


}