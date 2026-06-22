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
public class UtbCommentVO extends BaseVO {
    private String commentSeq;

    private String parentCommentSeq;

    private String commentType;

    private String commentContent;

    private String commentCode;

    // 조회 JOIN용
    private String createUserName;
    private String createUserDeptName;
    private String createUserProfileImageUrl;
}