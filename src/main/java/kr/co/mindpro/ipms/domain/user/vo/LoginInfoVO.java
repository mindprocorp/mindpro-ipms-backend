package kr.co.mindpro.ipms.domain.user.vo;

import java.time.OffsetDateTime;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 정보 테이블 매핑 객체
 * DB 테이블 "MP_IPMS_PA".utb_login_info 컬럼과 1:1 대응됩니다.
 *
 * @author   : intst
 * @fileName : LoginInfoVO.java
 * @since    : 2026. 01. 09.
 */
@Getter @Setter
public class LoginInfoVO extends BaseVO {

    private String userMstSeq;        // 사용자 마스터 일련번호 (PK)
    private String officeSeq;         // 사무소 일련번호
    private String officeEmployee_seq; // 사무소 직원 일련번호
    
    private String userId;            // 사용자 ID
    private String userPassword;      // 암호화된 비밀번호
    private OffsetDateTime passwordUpdateDate; // 비밀번호 변경일
    
    private String loginFailCount;    // 로그인 실패 횟수 (bpchar(1))
    private String emailAuthYn;       // 이메일 인증 여부 (bpchar(1))
    private String loginLockYn;       // 로그인 잠금 여부 (bpchar(1))
    
    private OffsetDateTime activeAt;  // 마지막 활성 일시
}
