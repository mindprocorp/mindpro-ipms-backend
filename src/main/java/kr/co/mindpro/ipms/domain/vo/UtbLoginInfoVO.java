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
public class UtbLoginInfoVO extends BaseVO {
    private String userMstSeq;

    private String officeSeq;

    private String officeEmployeeSeq;

    private String userId;

    private String userPassword;

    private LocalDateTime passwordUpdateDate;

    private String loginFailCount;

    private String emailAuthYn;

    private String loginLockYn;

    private LocalDateTime activeAt;
}