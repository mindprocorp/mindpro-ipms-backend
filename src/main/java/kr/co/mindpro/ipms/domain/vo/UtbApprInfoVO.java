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
public class UtbApprInfoVO extends BaseVO {
    private String apprSeq;

    private String taskCategoryCode;

    private String sendUser;

    private String handleUser;

    private String apprOpinion;

    private String interiorYn;

    private String externalYn;

    private String state;

    private LocalDateTime handleAt;
}