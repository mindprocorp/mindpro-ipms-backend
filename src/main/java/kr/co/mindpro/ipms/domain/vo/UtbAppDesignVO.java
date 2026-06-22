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
public class UtbAppDesignVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String multiViewDrawingFile;

    private String designDescription;

    private String designSummary;
}