package kr.co.mindpro.ipms.domain.gracePeriod.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.dto.response.CommonRecordResponse;
import lombok.Builder;

import java.time.OffsetDateTime;

public class GracePeriodResponse {

    @Builder
    public record DetailResponse(

            @Schema(description = "출원 seq", example = "PAT20260000005")
            String appSeq,

            @Schema(description = "공지예외 식별자", example = "GRCPRD20260000001")
            String gracePeriodSeq,

            @Schema(description = "공지예외 주장 내용", example = "공지예외 주장 내용입니다.")
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
