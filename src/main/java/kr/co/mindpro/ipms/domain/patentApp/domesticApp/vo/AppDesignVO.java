package kr.co.mindpro.ipms.domain.patentApp.domesticApp.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : AppDesignVO.java
 * @since : 2026. 1. 19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AppDesignVO extends BaseVO {

    /** 출원 mst seq */
    private String appSeq;

    /** 사무소 mst seq */
    private String officeSeq;

    /** 디자인 tbl seq */
    private String designSeq;

    /** 입체도면 파일 */
    private String multiViewDrawingFile;

    /** 디자인 설명 */
    private String designDescription;

    /** 디자인 요약 */
    private String designSummary;

    // 아래는 컬럼도 추가한 부분.

    /** 물품류 */
    private String goodsClass;

    /** 부분_디자인_여부 */
    private String isPartialDesign;

    /** 다의장(복수 디자인 수) */
    private String multiDesign;
}
