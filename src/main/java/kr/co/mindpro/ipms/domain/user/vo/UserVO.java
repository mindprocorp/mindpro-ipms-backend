package kr.co.mindpro.ipms.domain.user.vo;

import java.time.LocalDateTime;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : intst
 * @fileName	 : UserVO.java
 * @since	 : 2025. 12. 24.
 */
@Getter @Setter
public class UserVO extends BaseVO {
    private Long userSq;      // 고유 번호 (PK)
    private String userId;    // 아이디 (Unique)
    private String userPw;    // 암호화된 비밀번호
    private String userNm;    // 이름
    private String userRole;  // 권한 (ROLE_USER 등)
    private String useYn;     // 사용 여부 (Y/N)
    private LocalDateTime regDate;	// 등록 일시
    private LocalDateTime chgDate; 	// 수정 일시       
}