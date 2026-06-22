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
public class UtbBizInfoVO extends BaseVO {
    private String bizInfoSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String corpCode;

    private String ceoName;

    private String bizRegFile;

    private String bizRegNo;

    private String bizCorpName;

    private String bizAddr;

    private String bizAddrDetail;

    private String bizFaxNo;

    private String bizTelNo;

    private String bizPostNo;

    private String bizType;

    private String bizKind;

    private String discountCode;

    private LocalDateTime discountClosingDate;
}