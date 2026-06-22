package kr.co.mindpro.ipms.domain.gracePeriod.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;

import java.time.OffsetDateTime;

/**
 * @author : seokho
 * @fileName : GracePeriodSaveRequest.java
 * @since : 2026. 2. 6.
 */
public class GracePeriodRequest {
    public record SaveRequest(
            @Schema(description = "공지예외 seq")
            String gracePeriodSeq,

            @Schema(description = "출원 seq", example = "PAT20260000005")
            String appSeq,

            @Schema(description = "공지예외 주장 내용")
            CommonRecordResponse.CodeInfo gracePeriodContent,

            @Schema(description = "제출 마감일자", example = "2026-02-06")
            String submitDeadLineDate,

            @Schema(description = "제출일", example = "2026-02-06")
            String submitClosingDate,

            @Schema(description = "공지예외 주장일", example = "2026-02-06")
            String gracePeriodDate,

            @Schema(description = "비고", example = "비고 내용입니다.")
            String note
    ) {}
}
