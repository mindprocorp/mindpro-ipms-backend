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
public class UtbCommentMappVO extends BaseVO {
    private LocalDateTime mappingCommentSeq;

    private String boardCategoryCode;

    private String boardSeq;

    private String commentSeq;
}