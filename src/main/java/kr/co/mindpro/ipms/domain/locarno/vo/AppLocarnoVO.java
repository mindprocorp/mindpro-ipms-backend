package kr.co.mindpro.ipms.domain.locarno.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : mindpro
 * @fileName : AppLocarnoVO.java
 * @since : 2026. 2. 9.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppLocarnoVO extends BaseVO {

    // 사무소 시퀀스
    private String officeSeq;

    // 출원 시퀀스
    private String appSeq;

    // 로카르노 시퀀스
    private String locarnoSeq;

    // 물품류
    private String classNo;

    // 물품군
    private String subClassNo;

    // 로카르노 굿즈 시퀀스
    private String goodsSeq;

    // 로카르노 그룹 아이디
    private String locarnoGroupId;

    // 로카르노 국문명
    private String locarnoNameKo;

    // 로카르노 영문명
    private String locarnoNameEn;

    /** response용 속성들 */
    private String locarnoVersion;

    private String goodsNo;

    private String goodsSummaryKo;

    private String goodsSummaryEn;

    private int goodsCount;
}
