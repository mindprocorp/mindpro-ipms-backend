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
public class UtbCostExternalVO extends BaseVO {
    private String externalCostSeq;

    private String costSeq;

    private String outgoingCountryCode;

    private String incomingCountryCode;

    private String externalOfficeCode;

    private String externalInvoiceno;

    private String externalInvoiceFile;

    private String currency;

    private LocalDateTime exchangeDate;

    private Integer exchangeRatio;

    private Integer krwAmount;

    private Integer exchangeCost;
}