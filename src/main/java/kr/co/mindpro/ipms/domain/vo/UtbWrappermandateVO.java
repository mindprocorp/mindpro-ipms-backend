package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbWrappermandateVO extends BaseVO {
    private String wrappermandateSeq;

    private String customerinfoSeq;

    private String officeSeq;

    private String mandatePaperFile;

    private String mandateWrapperNo;

    private String patentCustomerNo;

    private String mandateRange;
}