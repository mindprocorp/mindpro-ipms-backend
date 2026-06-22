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
public class UtbDuedateMstVO extends BaseVO {
    private String duedateSeq;

    private LocalDateTime duedateDate;

    private String dueDateKindCode;

    private String alarmEstablishmentCode;

    private String alarmYn;

    private String alarmCompleteYn;

    private String alarmCategoryCode;
}