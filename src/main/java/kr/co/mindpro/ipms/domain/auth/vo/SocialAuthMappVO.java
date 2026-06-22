package kr.co.mindpro.ipms.domain.auth.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SocialAuthMappVO extends BaseVO {
    private String authSeq;
    private String authApproach;
    private String userMstSeq;
}
