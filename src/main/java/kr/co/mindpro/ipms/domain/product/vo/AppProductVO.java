package kr.co.mindpro.ipms.domain.product.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : ProductVO.java
 * @since : 2026. 2. 9.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppProductVO extends BaseVO {

    // 사무소 시퀀스
    private String officeSeq;

    // 출원 시퀀스
    private String appSeq;

    // 상품 시퀀스
    private String productSeq;

    // 상품 id
    private String productId;

    // 나이스 버전 정보
    private String niceVersion;

    // 상품 류
    private String productClass;

    // 상품 그룹 아이디
    private String productGroupId;

    // 상품 개수
    private int productCount;

    // 상품 국문명
    private String productNameKo;

    // 상품 영문명
    private String productNameEn;

    private String productSummaryKo;

    private String productSummaryEn;
}
