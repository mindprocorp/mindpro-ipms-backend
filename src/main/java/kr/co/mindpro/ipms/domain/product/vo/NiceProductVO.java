package kr.co.mindpro.ipms.domain.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 나이스 지정상품 마스터 매핑 객체
 * utb_product_mst(지정상품)의 컬럼을 관리합니다.
 *
 * @author   : seokho
 * @fileName : NiceProductVO.java
 * @since    : 2026. 2. 11.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "나이스 지정상품 마스터 정보 객체")
public class NiceProductVO extends BaseVO {

    @Schema(description = "상품 아이디 (product_id, PK)", example = "P000001")
    private String productId;

    @Schema(description = "나이스 버전 (nice_version)", example = "12-2024")
    private String niceVersion;

    @Schema(description = "분류 번호 (class_no)", example = "09")
    private String classNo;

    @Schema(description = "유사군 코드 (similarity_code)", example = "G0101")
    private String similarityCode;

    @Schema(description = "상품 명칭(국문) (product_nm_ko)", example = "스마트폰")
    private String productNmKo;

    @Schema(description = "상품 명칭(영문) (product_nm_en)", example = "Smart phones")
    private String productNmEn;

    @Schema(description = "비고 (note)")
    private String note;

    @Schema(description = "삭제 여부 (del_yn)", example = "N")
    private String delYn;
}
