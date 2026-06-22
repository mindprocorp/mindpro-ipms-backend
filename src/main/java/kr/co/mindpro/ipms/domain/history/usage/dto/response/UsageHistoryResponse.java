package kr.co.mindpro.ipms.domain.history.usage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : seokho
 * @fileName : UsageHistoryResponse.java
 * @since : 2026. 4. 29.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageHistoryResponse {
    @Schema(description = "사용이력 식별키")
    private String usageHistorySeq;

    @Schema(description = "사용자 식별키")
    private String userMstSeq;

    @Schema(description = "사무소 식별키")
    private String officeSeq;

    @Schema(description = "사용자 아이디 (조인 결과)")
    private String createUser;

    @Schema(description = "메뉴명")
    private String menuName;

    @Schema(description = "행위명 (actionType)")
    private String actionName;

    @Schema(description = "요청 URL")
    private String reqUrl;

    @Schema(description = "요청 메서드")
    private String reqMethod;

    @Schema(description = "클라이언트 IP")
    private String clientIp;

    @Schema(description = "User-Agent")
    private String userAgent;

    @Schema(description = "대상 클래스")
    private String targetClass;

    @Schema(description = "대상 메서드")
    private String targetMethod;

    @Schema(description = "삭제 여부")
    private String delYn;

    @Schema(description = "비고")
    private String note;

    @Schema(description = "사용일시 (YYYY-MM-DD HH:mm:ss)")
    private String createAt;
}
