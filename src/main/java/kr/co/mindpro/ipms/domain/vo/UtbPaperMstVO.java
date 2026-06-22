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
public class UtbPaperMstVO extends BaseVO {
    private String paperCategoryCode;

    private String paperFileSeq;

    private String submitstateCode;

    private String uploadUser;

    private String paperStepCode;
}