package kr.co.mindpro.ipms.domain.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 나이스 분류 마스터 매핑 객체
 * utb_nice_class_mst(상품류/서비스업류)의 컬럼을 관리합니다.
 *
 * @author   : intst
 * @fileName : NiceClassVO.java
 * @since    : 2026. 2. 11.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "나이스 상품류 및 서비스업류 마스터 정보 객체")
public class NiceClassVO extends BaseVO {

    // --- [Common Key & Status] 공통 키 및 상태 ---
    @Schema(description = "분류 번호 (class_no, PK)", example = "09")
    private String classNo;
    
    @Schema(description = "나이스 버전 (nice_version, PK)", example = "12-2024")
    private String niceVersion;

    @Schema(description = "카테고리 구분 (category_gb)", example = "PRODUCT")
    private String categoryGb;

    // --- [Names] 명칭 필드 ---
    @Schema(description = "분류 명칭(국문) (class_nm_ko)", example = "과학기기 등")
    private String classNmKo;

    @Schema(description = "분류 명칭(영문) (class_nm_en)", example = "Scientific instruments etc.")
    private String classNmEn;

    // --- [Descriptions] 설명 필드 ---
    @Schema(description = "분류 설명(국문) (class_desc_ko)")
    private String classDescKo;

    @Schema(description = "분류 설명(영문) (class_desc_en)")
    private String classDescEn;

    // --- [Status] 상태 필드 ---
    @Schema(description = "삭제 여부 (del_yn)", example = "N")
    private String delYn;

    /* 
     * [BaseVO 상속 필드 참고]
     * private String createUser;
     * private OffsetDateTime createAt;
     * private String updateUser;
     * private OffsetDateTime updateAt;
     */
}
