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
public class UtbAppProductVO extends BaseVO {
    private String officeSeq;

    private String appSeq;

    private String col6;

    private String productClass;

    private Integer productCount;

    private String productNameKo;

    private String productNameEn;
}