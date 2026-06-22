package kr.co.mindpro.ipms.domain.cost.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * 비용 테이블 매핑 객체
 * DB 테이블 cost_mst 의 컬럼과 1:1 대응됩니다.
 *
 * @author	 : min
 * @fileName	 : CostVO.java
 * @since	 : 2026. 01. 07.
 */
/**
 * 비용 통합 관리 객체
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "비용 통합 관리 객체 (Master + Mapping 통합)")
public class CostVO extends BaseVO {

    // --- [UtbCostMapp 관련 필드 : 관계 정보 및 연결 고리] ---

    @Schema(description = "사무소 일련번호", example = "OFFICE2026001")
    private String officeSeq;

    @Schema(description = "비용 매핑 일련번호 (Mapp PK)", example = "MAPCOST20260000001")
    private String mappingCostSeq;

    @Schema(description = "업무 일련번호 (특허/상표 등)", example = "PAT20260000005")
    private String tblSeq;

    @Schema(description = "비용 마스터 참조 일련번호 (Mst FK)", example = "COSTMST20260000001")
    private String costSeq;

    private int rowNum;


    // --- [UtbCostMst 관련 필드 : 실제 비용 본체 데이터] ---

    @Schema(description = "비용 카테고리 코드 (상세 구분)", example = "COST_FEE_01")
    private String costCategoryCode;

    @Schema(description = "구분 코드 값", example = "30")
    private String paymentDiv;

    @Schema(description = "출원번호(임시)")
    private String appNo;

    @Schema(description = "한화 금액", example = "150000")
    private Long krwAmount;

    @Schema(description = "할인율 (예: 30% 감면이면 30 입력)", example = "30")
    private Integer discountRatio;

    @Schema(description = "송금 횟수", example = "1")
    private Integer remittanceCount;

    @Schema(description = "비용 발생 날짜", example = "2026-01-15T10:00:00+09:00")
    private OffsetDateTime costDate;

    @Schema(description = "비용 송금 횟수 (상세)", example = "1")
    private String costRemittanceCount;

    @Schema(description = "비용 송금 날짜", example = "2026-01-20T15:00:00+09:00")
    private OffsetDateTime costRemittanceDate;

    @Schema(description = "비용 송금 여부", allowableValues = {"Y", "N"}, example = "N")
    private String costRemittanceYn;

    @Schema(description = "수수료", example = "50000")
    private Integer costFee;

    @Schema(description = "부가세", example = "5000")
    private Integer costVat;

    @Schema(description = "비고 (특이사항)", example = "해외 대리인 비용 포함")
    private String note;

}