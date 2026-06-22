package kr.co.mindpro.ipms.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author : seokho
 * @fileName : ProductRequest.java
 * @since : 2026. 2. 9.
 */
public class ProductRequest {

    public record SaveAllProductList(

            @Schema(description = "상품 리스트")
            List<SaveProduct> prodList,

            String productGroupId

    ) {}

    public record SaveProduct(

            @Schema(description = "출원 시퀀스")
            String appSeq,

            @Schema(description = "상품 id")
            String productId,

            @Schema(description = "nice 버전 값")
            String niceVersion,

            @Schema(description = "상품 류")
            String ProductClass,

            @Schema(description = "상품개수")
            int productCount,

            @Schema(description = "상품명(한글)")
            String productNameKo,

            @Schema(description = "상품명(영문)")
            String productNameEn,

            @Schema(description = "비고")
            String note
    ) {}
}
