package kr.co.mindpro.ipms.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 용자 관련 요청 객체
 * 각 용도별로 내부 record를 선언하여 관리 포인트의 효율성을 높임
 *
 * @author	 : intst
 * @fileName	 : UserRequest.java
 * @since	 : 2025. 12. 24.
 */
public class UserRequest {

	@Schema(description = "사용자 정보 수정 요청")
	public record Update(
	    @NotBlank(message = "수정할 이름은 필수입니다.")
	    @Schema(description = "사용자명(한글)", example = "김철수")
	    String userNameKo,

	    @Schema(description = "사용자명(영문)", example = "Kim Cheolsu")
	    String userNameEn,

	    @Email(message = "이메일 형식이 올바르지 않습니다.")
	    @Schema(description = "이메일", example = "user@example.com")
	    String userEmail,

	    @Schema(description = "휴대폰 번호", example = "01012345678")
	    String userMobileNo,

	    @Schema(description = "전화번호", example = "0212345678")
	    String userTelNo,

	    @Schema(description = "팩스번호", example = "0212345679")
	    String userFaxNo,

	    @Schema(description = "우편번호", example = "06621")
	    String userPostNo,

	    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
	    String userAddr,

	    @Schema(description = "상세 주소", example = "101동 1001호")
	    String userAddrDetail,

	    @Schema(description = "부서명", example = "특허팀")
	    String deptName,

	    @Schema(description = "직위", example = "대리")
	    String userPosition
	) {}
	
    @Schema(description = "비밀번호 변경 요청")
    public record ChangePassword(
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        @Schema(description = "현재 비밀번호", example = "pw123!")
        String currentPw,

        @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", 
                 message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
        @Schema(description = "새로운 비밀번호", example = "new1234!")
        String newPw
    ) {}
	
	@Schema(description = "신규 통합 회원가입 요청 (3개 테이블 저장)")
    public record RegisterIndividual(
        @Schema(description = "회원구분", example = "INDIVIDUAL")
        @NotBlank(message = "회원구분 코드는 필수입니다.")
        String userCategoryCode,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일 (ID로 사용)", example = "user@example.com")
        String userEmail,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(description = "비밀번호", example = "Password1!")
        String userPassword,

        @NotBlank(message = "성명은 필수입니다.")
        @Schema(description = "성명", example = "홍길동")
        String userName,

        @Schema(description = "전화번호", example = "01012345678")
        String mobileNo,

        @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
        String userAddr,

        @Schema(description = "상세 주소", example = "101동 1001호")
        String userAddrDetail,

        @Schema(description = "우편 번호", example = "06621")
        String userZipCode,

        @AssertTrue(message = "이용약관에 동의해야 합니다.")
        @Schema(description = "이용약관 동의", example = "true")
        boolean termsAgree,

        @AssertTrue(message = "개인정보처리방침에 동의해야 합니다.")
        @Schema(description = "개인정보처리방침 동의", example = "true")
        boolean privacyPolicyAgree,

        @Schema(description = "마케팅 수신 동의", example = "false")
        boolean marketingAgree,
        
        @Schema(description = "소속 사무소 ID", example = "PGOKR20260000001")
        String officeId,

        @Schema(description = "소셜 로그인 제공자 (KAKAO, GOOGLE 등)")
        String socialProvider,

        @Schema(description = "소셜 로그인 제공자 ID")
        String socialProviderId,

        @Schema(description = "소셜 이메일")
        String socialEmail
    ) {}
	
	@Schema(description = "신규 사업자 회원가입 요청")
    public record RegisterCorporate(
        @Schema(description = "회원구분", example = "CORPORATE")
        @NotBlank(message = "회원구분 코드는 필수입니다.")
        String userCategoryCode,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일 (ID로 사용)", example = "corp@company.com")
        String userEmail,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(description = "비밀번호", example = "Password1!")
        String userPassword,

        @NotBlank(message = "성명은 필수입니다.")
        @Schema(description = "담당자 성명", example = "홍길동")
        String userName,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Schema(description = "담당자 전화번호", example = "01012345678")
        String mobileNo,

        @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
        String userAddr,

        @Schema(description = "상세 주소", example = "101동 1001호")
        String userAddrDetail,

        @Schema(description = "우편 번호", example = "06621")
        String userZipCode,

        @AssertTrue(message = "이용약관에 동의해야 합니다.")
        @Schema(description = "이용약관 동의", example = "true")
        boolean termsAgree,

        @AssertTrue(message = "개인정보처리방침에 동의해야 합니다.")
        @Schema(description = "개인정보처리방침 동의", example = "true")
        boolean privacyPolicyAgree,

        @Schema(description = "마케팅 수신 동의", example = "false")
        boolean marketingAgree,

        @Valid
        @NotNull(message = "사업자 정보는 필수입니다.")
        @Schema(description = "사업자 상세 정보")
        CorpInfoRequest corpInfo,

        @Schema(description = "소셜 로그인 제공자 (KAKAO, GOOGLE 등)")
        String socialProvider,

        @Schema(description = "소셜 로그인 제공자 ID")
        String socialProviderId,

        @Schema(description = "소셜 이메일")
        String socialEmail
    ) {}

    @Schema(description = "사업자 상세 정보 요청")
    public record CorpInfoRequest(
        @NotBlank(message = "사업자 유형은 필수입니다.")
        @Schema(description = "사업자 유형 (GENERAL: 일반/변리사 등)", example = "GENERAL")
        String corpType,

        @NotBlank(message = "대표자명은 필수입니다.")
        @Schema(description = "대표자명", example = "이대표")
        String ceoName,

        @NotBlank(message = "회사명은 필수입니다.")
        @Schema(description = "회사명(상호)", example = "주식회사 테스트")
        String corpName,

        @Schema(description = "대표전화", example = "0212345678")
        String corpTel,

        @Schema(description = "팩스번호", example = "0212345679")
        String corpFax,

        @Schema(description = "사업장 주소", example = "서울특별시 서초구 서초대로 456")
        String corpAddr,

        @Schema(description = "사업장 상세 주소", example = "12층")
        String corpAddrDetail,

        @Schema(description = "사업장 우편번호", example = "06621")
        String corpZipCode,

        @NotBlank(message = "사업자등록번호는 필수입니다.")
        @Schema(description = "사업자등록번호", example = "1234567890")
        String corpRegNumber,

        @Schema(description = "사업자등록증 파일 경로/UUID", example = "file-uuid-1234")
        String businessLicenseFileUrl
    ) {}


    @Schema(description = "신규 인적 사항 단독 등록 요청 (UTB_USER_INFO 전용)")
    public record RegisterUserInfo(
            @NotBlank(message = "소속 사무소 ID는 필수입니다.")
            @Schema(description = "소속 사무소 ID", example = "PGOKR20260000001")
            String officeSeq,

            @NotBlank(message = "성명은 필수입니다.")
            @Schema(description = "사용자 성명", example = "홍길동")
            String userNameKo,

            @Email(message = "이메일 형식이 올바르지 않습니다.")
            @Schema(description = "이메일", example = "user@example.com")
            String userEmail,

            @Schema(description = "휴대폰 번호", example = "01012345678")
            String userMobileNo,

            @Schema(description = "우편번호", example = "06621")
            String userPostNo,

            @Schema(description = "주소", example = "서울특별시 강남구 테헤란로 123")
            String userAddr,

            @Schema(description = "상세 주소", example = "101동 1001호")
            String userAddrDetail,

            @Schema(description = "부서명", example = "특허팀")
            String deptName,

            @Schema(description = "직책", example = "팀장")
            String userPosition,

            @Schema(description = "직급", example = "1급")
            String jobGradeCode,

            @Schema(description = "직무", example = "특허")
            String workCode,

            @Schema(description = "등록자 아이디", example = "admin")
            String createUser
    ) {}

    public record UpdateEmployee(
            @NotBlank String userMstSeq,
            String userNameKo,
            String userEmail,
            String userMobileNo,
            String userAddr,
            String userAddrDetail,
            String userPostNo,
            String deptName,
            String userPosition,
            String positionCode,
            String jobGradeCode,
            String workCode,
            String userTypeCode,
            String workStatusCode,
            String employStatusCode,
            String acctStatusCode,
            String roleSeq,
            String updateUser
    ) {}

    public record UserDetailRequest(


                @Schema(description = "고안자(사용자) 일련번호", example = "USERIF20260000002")
                String userInfoSeq,
                @Schema(description = "고안자(사용자) 일련번호", example = "INVETR20260000001")
                String appInventorSeq,
                @Schema(description = "매핑된 사건번호 (출원/사건 SEQ)", example = "APPMST20260000394")
                @NotBlank(message = "사건번호는 필수입니다.")
                String tblSeq,

                // --- [성명 정보] ---
                @Schema(description = "고안자 성명(국문)", example = "홍길동")
                @NotBlank(message = "국문 성명은 필수입니다.")
                String userNameKo,

                @Schema(description = "고안자 성명(영문)", example = "Hong Gil Dong")
                String userNameEn,

                @Schema(description = "고안자 성명(한문)", example = "洪吉童")
                String userNameZh,

                @Schema(description = "고안자 성명(일문)", example = "ホン・ギルドン")
                String userNameJa,

                // --- [개인정보] ---
                @Schema(description = "주민등록번호 (또는 외국인등록번호)", example = "900101-1234567")
                String residentNo,

                @Schema(description = "국적 코드 (ISO 3166-1 alpha-2)", example = "KR")
                String countryCode,

                // --- [주소 정보] ---
                @Schema(description = "주소(국문)", example = "서울특별시 강남구 테헤란로 123")
                String userAddr,

                @Schema(description = "주소(영문)", example = "123, Teheran-ro, Gangnam-gu, Seoul, Korea")
                String userAddrEn,

                @Schema(description = "주소(한문)", example = "首爾特別市 江南區 ...")
                String userAddrZh,

                @Schema(description = "주소(일문)", example = "東京都新宿区...")
                String userAddrJa,

                // --- [매핑 정보] ---
                @Schema(description = "정렬 순서", example = "1")
                String sort,

                @Schema(description = "비고", example = "주발명자")
                String note

        ) {
            // 추가적인 로직이나 팩토리 메소드가 필요하면 여기에 작성합니다.
        }

    public record UserListRequest(
            String tblSeq,
            String userInfoSeq,
            String userNameKo,
            String userNameEn,
            String userNameZh,
            String userNameJa,
            String residentNo,
            String userAddr,
            Integer sortSeq
    ) {}
}
