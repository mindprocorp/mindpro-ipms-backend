package kr.co.mindpro.ipms.domain.ids.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author : seokho
 * @fileName : IdsRequest.java
 * @since : 2026. 3. 30.
 */
public class IdsRequest {
    public record SaveIdsRequest(
            @Schema(description = "출원 식별키", example = "APPMST20260000393")
            String appSeq,

            @Schema(description = "ids 식별키")
            String idsSeq,

            @Schema(description = "발생국가코드", example = "KR")
            String occurCountryCode,

            @Schema(description = "발생국가명")
            String occurCountryName,

            @Schema(description = "발생번호", example = "123123")
            String occurNo,

            @Schema(description = "영문패밀리번호", example = "123123")
            String familyNoEn,

            @Schema(description = "IDS 기제출 여부", example = "Y")
            String isIdsSubmitted,

            @Schema(description = "발생일", example = "20260101")
            String occurDate,

            @Schema(description = "공개일", example = "20260101")
            String idsPubDate,

            @Schema(description = "접수일", example = "20260101")
            String idsReceiptDate,

            @Schema(description = "IDS 발송일", example = "20260101")
            String idsSendDate,

            @Schema(description = "IDS 제출마감일", example = "20260101")
            String idsDeadline,

            @Schema(description = "IDS 제출일", example = "20260101")
            String idsSubmitDate,

            @Schema(description = "제출담당자", example = "USERIF20260000002")
            String idsSubmitMng,

            @Schema(description = "제출담당자명", example = "김철수")
            String idsSubmitMngNm,

            @Schema(description = "IDS 메모")
            String note
    ) {}
}
