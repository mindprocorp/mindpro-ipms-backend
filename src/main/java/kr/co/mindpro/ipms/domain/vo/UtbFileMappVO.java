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
public class UtbFileMappVO extends BaseVO {
    private String fileSeq;

    private String tblSeq;

    private String fileKindCode;

    private String fileCategoryCode;

    private String fileRepositorySeq;

    private Integer fileSize;
}