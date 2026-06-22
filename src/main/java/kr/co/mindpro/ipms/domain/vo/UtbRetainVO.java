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
public class UtbRetainVO extends BaseVO {
    private String retainSeq;

    private String officeSeq;

    private String retainTitle;

    private String retainContent;

    private String state;

    private String affiliation;

    private String corpStaffName;

    private String corpStaffPosition;

    private String corpStaffEmail;

    private String corpStaffMobile;

    private String corpStaffTel;

    private String corpStaffDept;

    private String officeEmployee;

    private String officeEmployeeMobile;

    private String officeEmployeeEmail;

    private String retainFile;

    private LocalDateTime retainDate;

    private String rightcategoryCode;

    private String appNameKo;

    private String appNameEn;

    private String interiorYn;

    private String externalYn;

    private LocalDateTime retainSendDate;

    private LocalDateTime retainRegDate;

    private String retainResult;
}