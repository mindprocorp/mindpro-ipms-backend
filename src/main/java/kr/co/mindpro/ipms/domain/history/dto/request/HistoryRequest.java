package kr.co.mindpro.ipms.domain.history.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class HistoryRequest {

    public record ModifiedHistDetail(
            @Schema(description = "이력 고유번호", example = "MH20231018001")
            String modifiedHistSeq,

            @Schema(description = "참조 테이블 일련번호", example = "CUST123456")
            String tblSeq,

            @Schema(description = "변경 전 값", example = "홍길동")
            String beforeValue,

            @Schema(description = "변경 후 값", example = "김철수")
            String afterValue,

            @Schema(description = "변경 항목(내용)", example = "고객명 변경")
            String modifiedContent,

            @Schema(description = "비고 내용", example = "오타 수정")
            String note,

            @Schema(description = "변경일자(수동지정용)", example = "2023-10-18")
            String modifiedDate
    ) {}
}
