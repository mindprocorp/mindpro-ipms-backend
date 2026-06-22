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
public class UtbProprresearchResultVO extends BaseVO {
    private String officeSeq;

    private String priorresearchSeq;

    private String priorresearchResult;

    private String resultCategoryCode;

    private String priorresearchResultFile;

    private LocalDateTime priorresearchSendDate;

    private String priorresearchResultContent;

    private String priorresearchResultTitle;
}