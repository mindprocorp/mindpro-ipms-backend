package kr.co.mindpro.ipms.domain.ids.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

/**
 * @author : mindpro
 * @fileName : IdsVO.java
 * @since : 2026. 3. 12.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class IdsVO extends BaseVO {
    @Schema(description = "사무소 식별키", example = "PGOKR20260000002")
    private String officeSeq;

    @Schema(description = "출원 식별키", example = "APPMST20260000393")
    private String appSeq;

    @Schema(description = "ids 식별키")
    private String idsSeq;

    @Schema(description = "발생국가코드", example = "KR")
    private String occurCountryCode;

    @Schema(description = "발생국가명")
    private String occurCountryName;

    @Schema(description = "발생번호", example = "123123")
    private String occurNo;

    @Schema(description = "영문패밀리번호", example = "123123")
    private String familyNoEn;

    @Schema(description = "IDS 기제출 여부", example = "Y")
    private String isIdsSubmitted;

    // 기일 관련 항목
    @Schema(description = "발생일", example = "20260101")
    private String occurDate;

    @Schema(description = "공개일", example = "20260101")
    private String idsPubDate;

    @Schema(description = "접수일", example = "20260101")
    private String idsReceiptDate;

    @Schema(description = "IDS 발송일", example = "20260101")
    private String idsSendDate;

    @Schema(description = "IDS 제출마감일", example = "20260101")
    private String idsDeadline;

    @Schema(description = "IDS 제출일", example = "20260101")
    private String idsSubmitDate;

    // 담당자 관련 항목
    @Schema(description = "제출담당자", example = "USERIF20260000002")
    private String idsSubmitMng;
    private String idsSubmitMngNm;

}
