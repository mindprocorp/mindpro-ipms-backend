package kr.co.mindpro.ipms.domain.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UtbAppSpecificationVO extends BaseVO {
    private String specificationSeq;

    private String appSeq;

    private String officeSeq;

    private String inventionName;

    private String techField;

    private String backgroundTech;

    private String problem;

    private String patentClaimRange;

    private String inventionEffect;

    private String drawingDescription;

    private String inventionContent;

    private String drawingFile;

    private String summaryFile;

    private String state;
}