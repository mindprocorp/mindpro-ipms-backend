package kr.co.mindpro.ipms.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 사용자 테이블 매핑 객체
 * DB 테이블 ipms_user 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : intst
 * @fileName	 : UserVO.java
 * @since	 : 2025. 12. 24.
 */
@Data
@SuperBuilder
@NoArgsConstructor  // MyBatis가 객체를 생성할 때 사용하는 기본 생성자 (필수)
@AllArgsConstructor // Builder 패턴과 MyBatis 전체 매핑을 위한 생성자
@Schema(description = "고안자 정보 VO")
public class UserInfoVO extends BaseVO {

    private String userInfoSeq;
    private String appInventorSeq;
    private String officeSeq;
    private String tblSeq;
    private String sort;
    private String userNameKo;
    private String userNameEn;
    private String userNameZh;
    private String userNameJa;
    private String userAddr;
    private String userAddrEn;
    private String userAddrZh;
    private String userAddrJa;
    private String userAddrDetail;
    private String userEmail;
    private String userMobileNo;
    private String residentNo; // 주민번호
    private String countryCode;
    private String note;

}