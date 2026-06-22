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
public class UtbBoardVO extends BaseVO {
    private String boardCategoryCode;

    private String boardSeq;

    private String boardTitle;

    private String boardContent;

    private String boardRole;

    private Integer searchCount;
}