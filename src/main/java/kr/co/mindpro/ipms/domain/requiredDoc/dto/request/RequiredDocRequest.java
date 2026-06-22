package kr.co.mindpro.ipms.domain.requiredDoc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : seokho
 * @fileName : RequiredDocRequest.java
 * @since : 2026. 4. 1.
 */
public class RequiredDocRequest {
    public record createRequiredDocRequest(
            @Schema(description = "구비서류 시퀀스")
            String requiredDocSeq,

            @Schema(description = "연결된 출원 시퀀스")
            String appSeq,

            @Schema(description = "구비서류")
            String requiredDocName,

            @Schema(description = "제출마감일")
            String submitDeadline,

            @Schema(description = "서명요청일")
            String signReqDate,

            @Schema(description = "접수일")
            String receiptDate,

            @Schema(description = "발송일")
            String sendDate,

            @Schema(description = "제출일")
            String submitDate
    ) {
    }
}
