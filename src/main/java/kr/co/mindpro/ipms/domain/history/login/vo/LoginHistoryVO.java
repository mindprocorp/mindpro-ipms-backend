package kr.co.mindpro.ipms.domain.history.login.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * @author : seokho
 * @fileName : LoginHistoryVO.java
 * @since : 2026. 4. 7.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistoryVO extends BaseVO {
    @Schema(description = "사무소 식별키")
    private String officeSeq;

    @Schema(description = "사용자 마스터 식별키 (PK-1)")
    private String userMstSeq;

    @Schema(description = "로그인 이력 식별키 (PK-2)")
    private String loginHistorySeq;

    @Schema(description = "이력 구분 (로그인, 로그아웃, 기타)")
    private String category;

    @Schema(description = "로그인 IP 주소 (inet)")
    private String loginIp;

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "사용자 한글 이름")
    private String userNameKo;

    @Schema(description = "사용자 영어 이름")
    private String userNameEn;

    @Schema(description = "사용자 한문 이름")
    private String userNameZh;

    @Schema(description = "로그인 성공 여부 (Y/N)")
    private String loginSuccessYn;

    @Schema(description = "접속 기기 유형 (PC, MOBILE 등)")
    private String loginDeviceType;

    @Schema(description = "접속 국가 코드")
    private String loginCountry;

    @Schema(description = "로그인 유형 (사번, SNS 등)")
    private String loginType;
}
