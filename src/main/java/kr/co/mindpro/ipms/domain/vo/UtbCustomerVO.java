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
public class UtbCustomerVO extends BaseVO {
    private String customerinfoSeq;

    private String officeSeq;

    private String customerPatentRegNo;

    private String customerCategoryCode;

    private String appPersonCategoryCode;

    private String corpCategoryCode;

    private String userCategoryCode;

    private String countryCode;

    private String appPostNo;

    private String customerPostNo;

    private String appAddr;

    private String customerAddr;

    private String customerFax;

    private String customerEmail;

    private String customerTelNo;

    private String mandatePaperFile;

    private String mandateWrapperNo;

    private String discountTargetYn;

    private String discountReason;

    private String discountPaperFile;

    private LocalDateTime discountStartDate;

    private LocalDateTime discountClosingDate;
}