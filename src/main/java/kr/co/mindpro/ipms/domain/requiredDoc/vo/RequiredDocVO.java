package kr.co.mindpro.ipms.domain.requiredDoc.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : RequiredDocVO.java
 * @since : 2026. 4. 1.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequiredDocVO extends BaseVO {
    private String officeSeq;

    private String appSeq;

    private String requiredDocSeq;

    private String requiredDocName;

    private String submitDeadline;

    private String signReqDate;

    private String receiptDate;

    private String sendDate;

    private String submitDate;
}
