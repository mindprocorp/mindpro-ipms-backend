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
public class UtbInvoiceBankingVO extends BaseVO {
    private String bankingSeq;

    private String participantSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String bankingCategory;

    private LocalDateTime depositDate;

    private Integer depositAmount;

    private String depositName;

    private String depositBank;

    private String depositAccountno;
}