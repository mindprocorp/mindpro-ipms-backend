package kr.co.mindpro.ipms.domain.locarno.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 로카르노 통합 마스터 매핑 객체
 * utb_locarno_mst(물품류)와 utb_locarno_subclass_mst(소분류)의 모든 컬럼을 관리합니다.
 *
 * @author   : mindpro
 * @fileName : LocarnoVO.java
 * @since    : 2026. 2. 9.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "로카르노 물품류 및 소분류 통합 정보 객체")
public class LocarnoVO extends BaseVO {

    // --- [Common Key & Status] 공통 키 및 상태 ---
    @Schema(description = "물품류 번호 (PK)", example = "07")
    private String classNo;
    
    @Schema(description = "로카르노 버전 (PK)", example = "15-2025")
    private String locarnoVersion;

    // --- [Mst: utb_locarno_mst] 물품류 전용 필드 ---
    @Schema(description = "카테고리 구분 (category_gb)")
    private String categoryGb;

    @Schema(description = "물품류 명칭(국문) (class_nm_ko)")
    private String classNmKo;

    @Schema(description = "물품류 명칭(영문) (class_nm_en)")
    private String classNmEn;

    @Schema(description = "물품류 설명(국문) (class_desc_ko)")
    private String classDescKo;

    @Schema(description = "물품류 설명(영문) (class_desc_en)")
    private String classDescEn;

    // --- [Sub: utb_locarno_subclass_mst] 소분류 전용 필드 ---
    @Schema(description = "소분류 번호 (subclass_no, PK)", example = "01")
    private String subclassNo;

    @Schema(description = "소분류 명칭(국문) (subclass_nm_ko)")
    private String subclassNmKo;

    @Schema(description = "소분류 명칭(영문) (subclass_nm_en)")
    private String subclassNmEn;

    // --- [Goods: utb_locarno_goods_mst] 물품 전용 필드 ---
    @Schema(description = "물품 일련번호 (PK)", example = "101")
    private Integer goodsSeq;

    @Schema(description = "물품 번호", example = "G001")
    private String goodsNo;

    @Schema(description = "물품 명칭(국문)", example = "가정용 세탁기")
    private String goodsNmKo;

    @Schema(description = "물품 명칭(영문)", example = "Washing machines for household use")
    private String goodsNmEn;    

    /* 
     * [BaseVO 상속 필드 참고]
     * private String createUser;
     * private OffsetDateTime createAt;
     * private String updateUser;
     * private OffsetDateTime updateAt;
     */
}
