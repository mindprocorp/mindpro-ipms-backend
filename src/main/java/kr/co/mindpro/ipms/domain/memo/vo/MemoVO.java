package kr.co.mindpro.ipms.domain.memo.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 기일 테이블 매핑 객체
 * DB 테이블 Duedate_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : DuedateVO.java
 * @since	 : 2026. 01. 07.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoVO extends BaseVO {

    // --- [utb_memo_mapp 관련 필드] ---
    private String officeSeq;
    private String tblSeq;
    private String tblCode;

    // --- [utb_memo 관련 필드] ---
    private String memoSeq;
    private String mustReadYn;
    private String memoTitle;
    private String memoRegDate;
    private String memoUserName;
    private String customerName;
    private String note;
}