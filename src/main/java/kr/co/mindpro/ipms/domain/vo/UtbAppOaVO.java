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
public class UtbAppOaVO extends BaseVO {
    private String appSeq;

    private String officeSeq;

    private String oaSeq;

    private Integer oaIndex;

    private String paperCategoryCode;

    private String oaPaperFile;

    private LocalDateTime deadLineDate;

    private String retouchWriteyn;

    private String staff;

    private String mainPatAttorney;

    private String originalFile;

    private String updateFile;

    private String state;
}