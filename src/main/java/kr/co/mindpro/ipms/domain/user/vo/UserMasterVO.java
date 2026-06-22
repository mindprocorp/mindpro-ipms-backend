package kr.co.mindpro.ipms.domain.user.vo;

import java.time.OffsetDateTime;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * [VO] 사용자 통합 마스터 매핑 객체
 * UTB_USER_MST, UTB_USER_INFO, UTB_LOGIN_INFO + UTB_OFFICE_MST, UTB_BIZ_INFO 통합 VO
 *
 * @author    : intst
 * @fileName : UserMasterVO.java
 * @since    : 2026. 01. 08.
 */
@Getter 
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class UserMasterVO extends BaseVO {

    // --- UTB_OFFICE_MST (사무소 마스터) [추가] ---
    private String officeSeq;           // 소속 사무소 일련번호 (PK) - selectKey 생성
    private String officeShortName;     // 사무소 약칭 (회사명)
    private String officeAddr;          // 사무소 주소
    private String officeTel;           // 사무소 전화번호
    private String officeAuthYn;        // 사무소 인증 여부
    private String officeStateCode;     // 사무소 상태 코드
    private String officeInviteCode;    // 회사 초대 코드
    private String planSeq;             // 사무소 구독 플랜 (utb_office_mst.plan_seq)

    // --- UTB_USER_MST (Master) ---
    private String userMstSeq;          // 사용자 마스터 일련번호 (PK) - selectKey 생성
    private String userCategoryCode;    // 사용자 구분 코드 (INDIVIDUAL, CORPORATE 등)
    private String mobileAuthYn;        // 휴대폰 인증 여부
    private String privacypolicyAgree;  // 개인정보처리방침 동의 여부
    private String termsAgree;          // 이용약관 동의 여부
    private String marketingAgree;      // 마케팅 동의 여부

    // --- UTB_USER_INFO (Detail) ---
    private String userInfoSeq;         // 사용자 정보 일련번호 (PK) - selectKey 생성
    private String userNameKo;          // 성명(한글)
    private String userNameEn;          // 성명(영문)
    private String userNameZh;          // 성명(중문)
    private String userAddr;            // 주소
    private String userAddrDetail;      // 상세 주소
    private String userEmail;           // 이메일
    private String corpCode;            // 회사 코드
    private String userMobileNo;        // 휴대폰 번호
    private String userFaxNo;           // 팩스 번호
    private String userTelNo;           // 전화 번호
    private String userPostNo;          // 우편 번호
    private String passportNo;          // 여권 번호
    private String countryCode;         // 국가 코드
    private String deptName;            // 부서명
    private String userPosition;        // 직책
    private String etaxYn;              //전자세금계산 담당자 여부
    private String profileImageUrl;     // 프로필 이미지 URL
    
    // 신규 추가된 상태값 컬럼
    private String userTypeCode;
    private String userTypeName;
    private String workStatusCode;
    private String workStatusName;
    private String employStatusCode;
    private String employStatusName;
    private String acctStatusCode;
    private String acctStatusName;
    private String roleSeq;
    private String roleName;
    private String roleType;  // SUPER_ADMIN / SYSTEM_ADMIN / SYSTEM_USER / CUSTOM (CommonService에서 조회 후 세팅)

    // --- UTB_LOGIN_INFO (Auth) ---
    private String userId;              // 로그인 아이디 (이메일)
    private String userPassword;        // 암호화된 비밀번호
    private OffsetDateTime passwordUpdateDate; // 비밀번호 변경일
    private String loginFailCount;      // 로그인 실패 횟수
    private String emailAuthYn;         // 이메일 인증 여부
    private String loginLockYn;         // 계정 잠금 여부
    private OffsetDateTime activeAt;    // 활성화 일시
    private String empSeq;              // 사원 일련번호

    // --- UTB_BIZ_INFO (사업자 상세 정보) [추가] ---
    private String bizInfoSeq;          // 사업자 정보 일련번호 (PK) - selectKey 생성
    private String bizRegNo;            // 사업자 등록 번호 (request.corpRegNumber 매핑)
    private String ceoName;             // 대표자명
    private String bizCorpName;         // 사업자 상호명
    private String bizRegFile;          // 사업자 등록증 파일 (UUID/URL)
    private String bizAddr;             // 사업장 주소
    private String bizAddrDetail;       // 사업장 상세 주소
    private String bizPostNo;           // 사업장 우편 번호
    private String bizTelNo;            // 사업장 전화 번호
    private String bizFaxNo;            // 사업장 팩스 번호
    private String bizType;             // 업태 (GENERAL 등)
    private String bizKind;             // 종목
    private OffsetDateTime discountClosingDate; // 할인 종료일

    // --- UTB_OFFICE_EMPLOYEE (사무소 직원) ---
    private String officeEmployeeSeq;       // 사무소 직원 일련번호 (PK) - selectKey 생성
    private String adminAuth;               // 관리자 권한
    private String officeEmployeePosition;  // 직책
    private String officeEmployeeDept;      // 부서
    private String workCode;                // 직무 코드
    private String positionCode;            // 직위 코드
    private String jobGradeCode;            // 직급 코드
    private String orgCode;                 // 조직 코드
    private String deptSeq;                 // 부서 일련번호 (FK)
}