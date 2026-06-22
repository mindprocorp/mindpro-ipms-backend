package kr.co.mindpro.ipms.domain.vo;

import java.time.LocalDateTime;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UtbLoginHistoryVO extends BaseVO {
    private String userMstSeq;

    private String loginHistorySeq;

    private Object loginIp;

    private LocalDateTime loginAt;

    private String loginSuccessYn;

    private String loginDeviceType;

    private String loginCountry;

    private String loginType;
}