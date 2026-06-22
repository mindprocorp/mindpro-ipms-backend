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
public class UtbAppPreferenceVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String preferenceSeq;

    private String priorCountryCode;

    private LocalDateTime preferenceAssertDate;

    private String preferenceNo;

    private String wipoCategoryCode;

    private String preferenceSearch;

    private String fullContent;

    private String regDate;

    private LocalDateTime submitDeadLineDate;

    private LocalDateTime submitClosingDate;
}