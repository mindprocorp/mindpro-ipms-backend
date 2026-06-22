package kr.co.mindpro.ipms.domain.vo;

import java.time.LocalDateTime;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbInvoiceMstVO extends BaseVO {
    private String participantSeq;

    private String userMstSeq;

    private String userInfoSeq;

    private String officeSeq;

    private String invoiceCategoryCode;

    private String invoiceKindCode;

    private LocalDateTime invoiceRegDate;

    private LocalDateTime invoiceSendDate;

    private String invoiceRegCategory;

    private String invoiceContent;

    private Long invoiceVat;

    private Long invoiceOfficialCost;

    private LocalDateTime invoiceOfficialCostRemittanceDate;

    private Long invoiceFee;

    private Long invoiceEtcCost;

    private String invoiceEtcCostContent;

    private String giveUpYn;

    private Long giveUpAmount;

    private String giveUpReason;
}