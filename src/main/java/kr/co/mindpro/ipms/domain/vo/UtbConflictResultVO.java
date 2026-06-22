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
public class UtbConflictResultVO extends BaseVO {
    private String conflictSeq;

    private String conflictResultSeq;

    private String participantSeq;

    private String userMstSeq;

    private String userInfo;

    private String officeSeq;

    private String judgmentNo;

    private LocalDateTime appealDeadLineDate;

    private String judgmentContent;

    private String judgmentFile;

    private String judgmentCategoryCode;
}