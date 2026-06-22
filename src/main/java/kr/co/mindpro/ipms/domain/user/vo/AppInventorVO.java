package kr.co.mindpro.ipms.domain.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

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
@Schema(description = "기일 정보 요약 응답")
public class AppInventorVO extends BaseVO {
    private String appInventorSeq; // 매핑 일련번호 (PK)
    private String officeSeq;       // 사무소 코드 (PK)
    private String tblSeq;          // 사건 번호 (app_seq 등)
    private String userInfoSeq;     // 사용자 정보 번호 (FK)

    private Integer sort;        // 순번
    private String note;            // 비고

}