package kr.co.mindpro.ipms.domain.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * [VO] 포괄위임 정보 객체
 * 테이블: utb_wrappermandate
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WrapperMandateVO extends BaseVO {

    @Schema(description = "위임 일련번호")
    private String wrappermandateSeq;

    @Schema(description = "고객 일련번호")
    private String customerSeq;

    @Schema(description = "사무소 일련번호")
    private String officeSeq;

    @Schema(description = "변리사명")
    private String attorneyName;

    @Schema(description = "지정변리사")
    private String designatedAttorney;

    @Schema(description = "대리인번호")
    private String agentNo;

    @Schema(description = "위임일 (YYYYMMDD)")
    private String mandateDate;

    @Schema(description = "위임번호 (포괄위임번호)")
    private String mandateWrapperNo;

    @Schema(description = "특허고객번호")
    private String patentCustomerNo;

    @Schema(description = "위임범위")
    private String mandateRange;

    @Schema(description = "정렬 순서")
    private Integer sortOrder;

    @Schema(description = "비고")
    private String note;
}