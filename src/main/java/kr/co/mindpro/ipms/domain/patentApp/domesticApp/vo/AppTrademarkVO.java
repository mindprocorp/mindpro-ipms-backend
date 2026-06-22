package kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : AppTrademarkVO.java
 * @since : 2026. 1. 20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppTrademarkVO extends BaseVO {

    /** 출원_식별자 */
    private String appSeq;

    /** 사무소_식별자 */
    private String officeSeq;

    /** 상표 tbl seq */
    private String trademarkSeq;

    /** 상표_이미지_파일 */
    private String trademarkImageFile;

    /** 마드리드_출원번호 */
    private String madridAppNo;

    /** 분류 출원 번호 */
    private String classificAppNo;

    /** 물품류 */
    private String goodsClass;
}
