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
public class UtbAppLocarnoVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String locarnoSeq;

    private String locarnoNameKo;

    private String locarnoNameEn;

    private String locarnoClass;

    private String subClass;
}