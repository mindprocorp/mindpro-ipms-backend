package kr.co.mindpro.ipms.domain.user.dto.response;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.domain.system.vo.RoleMenuMapVO;
import kr.co.mindpro.ipms.domain.user.vo.LoginInfoVO;
import kr.co.mindpro.ipms.domain.user.vo.UserMasterVO;
import kr.co.mindpro.ipms.domain.user.vo.UserVO;
import lombok.Builder;
import org.springframework.util.StringUtils;
import java.util.List;
import lombok.Builder;
import org.springframework.util.StringUtils;

/**
 * [DTO] 사용자 정보 응답 객체
 * 3개 통합 테이블(MST, INFO, LOGIN)의 정보를 안전하게 전달합니다.
 *
 * @author	 : intst
 * @fileName : UserResponse.java
 * @since	 : 2026. 01. 06. (수정)
 */
@Builder
@Schema(description = "사용자 정보 응답 데이터")
public record UserResponse(
    @Schema(description = "사용자 마스터 일련번호") String userMstSeq,
    @Schema(description = "사용자 정보 일련번호") String userInfoSeq,
    @Schema(description = "사용자 아이디(이메일)") String userId,
    @Schema(description = "사용자 이름(한글)") String userNameKo,
    @Schema(description = "사용자 이름(영문)") String userNameEn,
    @Schema(description = "이메일") String userEmail,
    @Schema(description = "휴대폰 번호") String userMobileNo,
    @Schema(description = "전화번호") String userTelNo,
    @Schema(description = "팩스번호") String userFaxNo,
    @Schema(description = "회원 구분 코드") String userCategoryCode,
    @Schema(description = "우편번호") String userPostNo,
    @Schema(description = "주소") String userAddr,
    @Schema(description = "상세 주소") String userAddrDetail,
    @Schema(description = "부서명") String deptName,
    @Schema(description = "직위") String userPosition,
    @Schema(description = "프로필 이미지 URL") String profileImageUrl,
    @Schema(description = "사용 여부") String useYn,
    @Schema(description = "소속 사무소 ID", example = "PGOKR20260000001") String officeId,
    @Schema(description = "등록 일시") LocalDateTime createAt,
    // office_employee
    @Schema(description = "사원번호") String officeEmployeeSeq,
    @Schema(description = "관리자 권한") String adminAuth,
    @Schema(description = "직책(조직)") String officeEmployeePosition,
    @Schema(description = "부서(조직)") String officeEmployeeDept,
    @Schema(description = "직무 코드") String workCode,
    @Schema(description = "직위 코드") String positionCode,
    @Schema(description = "직급 코드") String jobGradeCode,
    @Schema(description = "부서 일련번호") String deptSeq,
    @Schema(description = "사용자 유형") CodeItemInfo userType,
    @Schema(description = "근무 상태") CodeItemInfo workStatus,
    @Schema(description = "재직 상태") CodeItemInfo employStatus,
    @Schema(description = "계정 상태") CodeItemInfo acctStatus,
    @Schema(description = "권한 그룹") CodeItemInfo role,
    @Schema(description = "권한별 접근가능 메뉴 리스트") List<RoleMenuMapVO> menus
) {
	@Schema(description = "코드/명칭/타입 객체")
	public record CodeItemInfo(String code, String name, String type) {}

    private static CodeItemInfo buildCodeItem(String code, String name) {
        if (!StringUtils.hasText(code)) return null;
        return new CodeItemInfo(code, name != null ? name : "", null);
    }

    private static CodeItemInfo buildCodeItem(String code, String name, String type) {
        if (!StringUtils.hasText(code)) return null;
        return new CodeItemInfo(code, name != null ? name : "", type);
    }
	
    /**
     * LoginInfoVO를 Response DTO로 변환하는 정적 팩토리 메서드
     */
    public static UserResponse from(LoginInfoVO vo) {
        if (vo == null) return null;

        return UserResponse.builder()
                .userMstSeq(vo.getUserMstSeq())
                .userId(vo.getUserId())
                .officeId(vo.getOfficeSeq())
                .useYn("N".equalsIgnoreCase(vo.getDelYn()) ? "Y" : "N")
                .createAt(convertDateTime(vo.getCreateAt()))
                .build();
    }   

    /**
     * UserMasterVO를 Response DTO로 변환하는 정적 팩토리 메서드
     */
    public static UserResponse from(UserMasterVO vo) {
        return from(vo, null);
    }

    /**
     * UserMasterVO 및 접근 가능 메뉴 리스트를 Response DTO로 변환하는 정적 팩토리 메서드
     */
    public static UserResponse from(UserMasterVO vo, List<RoleMenuMapVO> menus) {
        if (vo == null) return null;

        return UserResponse.builder()
                .userMstSeq(vo.getUserMstSeq())
                .userInfoSeq(vo.getUserInfoSeq())
                .userId(vo.getUserId())
                .userNameKo(vo.getUserNameKo())
                .userNameEn(vo.getUserNameEn())
                .userEmail(vo.getUserEmail())
                .userMobileNo(vo.getUserMobileNo())
                .userTelNo(vo.getUserTelNo())
                .userFaxNo(vo.getUserFaxNo())
                .userCategoryCode(vo.getUserCategoryCode())
                .userPostNo(vo.getUserPostNo())
                .userAddr(vo.getUserAddr())
                .userAddrDetail(vo.getUserAddrDetail())
                .deptName(vo.getDeptName())
                .userPosition(vo.getUserPosition())
                .profileImageUrl(vo.getProfileImageUrl())
                .officeId(vo.getOfficeSeq())
                // 논리 삭제 여부(delYn)를 사용 여부(useYn)로 역산 (Null-safe 처리)
                .useYn("N".equalsIgnoreCase(vo.getDelYn()) ? "Y" : "N")
                .createAt(convertDateTime(vo.getCreateAt()))
                .officeEmployeeSeq(vo.getOfficeEmployeeSeq())
                .adminAuth(vo.getAdminAuth())
                .officeEmployeePosition(vo.getOfficeEmployeePosition())
                .officeEmployeeDept(vo.getOfficeEmployeeDept())
                .workCode(vo.getWorkCode())
                .positionCode(vo.getPositionCode())
                .jobGradeCode(vo.getJobGradeCode())
                .deptSeq(vo.getDeptSeq())
                .userType(buildCodeItem(vo.getUserTypeCode(), vo.getUserTypeName()))
                .workStatus(buildCodeItem(vo.getWorkStatusCode(), vo.getWorkStatusName()))
                .employStatus(buildCodeItem(vo.getEmployStatusCode(), vo.getEmployStatusName()))
                .acctStatus(buildCodeItem(vo.getAcctStatusCode(), vo.getAcctStatusName()))
                .role(buildCodeItem(vo.getRoleSeq(), vo.getRoleName(), vo.getRoleType()))
                .menus(menus)
                .build();
    }

    /**
     * [기존 유지용] 기존 UserVO와의 호환성이 필요한 경우 사용
     * 필드 타입이 달라진 부분은 적절히 매핑합니다.
     */
    public static UserResponse from(UserVO vo) {
        if (vo == null) return null;

        return UserResponse.builder()
                .userMstSeq(String.valueOf(vo.getUserSq())) // Long을 String으로 변환
                .userId(vo.getUserId())
                .userNameKo(vo.getUserNm())
                .useYn(vo.getUseYn())
                .createAt(vo.getRegDate())
                .build();
    }
    
    /**
     * OffsetDateTime을 LocalDateTime으로 안전하게 변환하는 내부 헬퍼 메서드
     */
    private static LocalDateTime convertDateTime(Object dateTime) {
        if (dateTime == null) return null;
        if (dateTime instanceof OffsetDateTime odt) return odt.toLocalDateTime();
        if (dateTime instanceof LocalDateTime ldt) return ldt;
        return null;
    }



}