package kr.co.mindpro.ipms.domain.organization.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class OfficeCodeVO extends BaseVO {
    private String officeCodeSeq;
    private String officeSeq;
    private String codeClass;
    private String officeCode;
    private String codeName;
    private String codeNameEn;
    private String refVal1;
    private String refVal2;
    private String sortOrd;
    private String useYn;
}
