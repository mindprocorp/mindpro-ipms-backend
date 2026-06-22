package kr.co.mindpro.ipms.domain.product.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * @author : seokho
 * @fileName : ProductResponse.java
 * @since : 2026. 2. 9.
 */
public class ProductResponse {

    @Builder
    public record ListDetail(

            String appSeq,

            String productGroupId,

            String productClass,

            int productCount,

            String productSummaryKo,

            String productSummaryEn

    ) {}

    @Builder
    public record Detail(
            String productGroupId,

            String productClass,

            List<ProdInfo> prodList
    ) {}

    public record ProdInfo(
            String productId,
            String niceVersion,
            String productSummaryKo,
            String productSummaryEn,
            String note
    ) {}
}
