package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UtbAppMstVO extends BaseVO {
    private String officeSeq;

    private String appSeq;

    private String retainSeq;

    private String appState;

    private String state;

    private String countryCode;

    private String rightCategory;

    private String productClass;

    private String appNameKo;

    private String appNameEn;

    private String outsourcingCorpName;

    private String outsourcingYn;

    private String giveUpContent;

    private String appCategory;

    private String appName;

    private String grade;

    private String independentClaim;

    private String dependentClaim;

    private Integer drawingPaperCount;

    private Integer ultiDependentClaimCount;

    private String appNo;

    private String originalAppNo;

    private String originalRegNo;

    private String doubleAppNo;

    private String globalAppNo;

    private String globalRegNo;

    private String regNo;

    private String openNo;

    private String productClassAppNo;

    private String reAppNo;

    private String publicNo;

    private String regPublicNo;

    private String madridNo;

    private String ipcCategoryCode;

    private String externalAppApproach;

    private String yearCntManagementYn;

    private String externalAppYn;

    private String trademarkResearchYn;

    private String renewalManagementYn;

    private String interiorPreferenceAssertYn;

    private String mandatePaperSubmitYn;

    private String delyn;
}