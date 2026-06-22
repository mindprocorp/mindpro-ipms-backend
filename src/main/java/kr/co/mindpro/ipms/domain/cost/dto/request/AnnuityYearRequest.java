package kr.co.mindpro.ipms.domain.cost.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

/**
 * @author : seokho
 * @fileName : AnnuityYearRequest.java
 * @since : 2026. 3. 16.
 * @description : 연차관리 탭 관련 VO.
 */
public class AnnuityYearRequest {
    public record AnnuityYearTabRequest(
            @Schema(description = "업무 일련번호 (특허/상표 등)", example = "APPMST20260000393")
            String tblSeq,

            @Schema(description = "비용 마스터 참조 일련번호 (Mst FK)")
            String costSeq,

            @Schema(description = "납부차수", example = "4")
            Integer remittanceCount,

            @Schema(description = "연차료 납부입", example = "2026-01-20")
            String costRemittanceDate,

            @Schema(description = "연차료납부액", example = "50000")
            Integer costFee,

            @Schema(description = "감면율 (예: 30% 감면이면 30 입력)", example = "30")
            Integer discountRatio,

            @Schema(description = "비고 (특이사항)", example = "해외 대리인 비용 포함")
            String note
    ) {}

}
