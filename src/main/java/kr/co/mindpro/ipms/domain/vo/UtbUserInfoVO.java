package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UtbUserInfoVO extends BaseVO {
    private String userInfoSeq;

    private String userMstSeq;

    private String officeSeq;

    private String userNameKo;

    private String userNameEn;

    private String userNameZh;

    private String userAddr;

    private String userAddrDetail;

    private String userEmail;

    private String corpCode;

    private String userMobileNo;

    private String userFaxNo;

    private String userTelNo;

    private String userPostNo;

    private String passportNo;

    private String countryCode;
}