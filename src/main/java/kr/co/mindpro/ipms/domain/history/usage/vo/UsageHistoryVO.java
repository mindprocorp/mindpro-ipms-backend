package kr.co.mindpro.ipms.domain.history.usage.vo;

import kr.co.mindpro.ipms.common.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : seokho
 * @fileName : UsageHistoryVO.java
 * @since : 2026. 4. 7.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsageHistoryVO extends BaseVO {

    // 식별자
    private String usageHistorySeq; // 사용 이력 PK
    private String userMstSeq;      // 사용자 식별키
    private String officeSeq;       // 사무소 식별키

    // 메뉴/액션 정보 (스웨거 어노테이션에서 파싱)
    private String menuName;        // 메뉴명 (예: 비용 관리)
    private String actionName;      // 행위명 (예: 리스트 조회)

    // HTTP 요청 정보
    private String reqUrl;          // 요청 URL
    private String reqMethod;       // HTTP 메서드 (GET, POST 등)
    private String clientIp;        // 접속자 IP (PostgreSQL inet 타입은 Java에서 String으로)
    private String userAgent;       // 브라우저/OS 정보

    // 시스템 타겟 정보
    private String targetClass;     // 타겟 클래스명 (도메인부터 시작)
    private String targetMethod;    // 타겟 메서드명
}
