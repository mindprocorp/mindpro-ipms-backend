package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * AI가 실제 실행 중인 프론트엔드/백엔드 서버에 HTTP GET 요청을 보낼 수 있는 도구.
 * - 프론트엔드가 expose하는 라우트 manifest, sitemap, API 등 동적으로 파악 가능
 * - 내부 허용 도메인만 호출 가능 (보안)
 */
@Slf4j
@Component
public class HttpRequestTool {

    @Value("${app.front-url:http://localhost:4000}")
    private String frontUrl;

    // 허용할 도메인 prefix 목록 (외부 임의 호출 방지)
    private static final List<String> ALLOWED_PREFIXES = List.of(
            "http://localhost",
            "http://192.168.",
            "https://ipms.mindpro.co.kr",
            "http://ipms.mindpro.co.kr"
    );

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Tool(description = """
        실행 중인 프론트엔드 또는 백엔드 서버에 HTTP GET 요청을 보내 응답 내용을 가져옵니다.
        주요 용도:
        - 프론트엔드가 노출하는 라우트 목록 또는 sitemap 확인
        - API 엔드포인트 응답 내용 확인
        - 실제 실행 중인 서버의 설정값 파악
        허용 도메인: localhost, 192.168.x.x, ipms.mindpro.co.kr (그 외 외부 URL 호출 불가)
        """)
    public String httpGet(
            @ToolParam(description = "요청할 URL. 예: http://localhost:4000/route-manifest.json") String url,
            @ToolParam(description = "응답 최대 길이 (기본 3000자, 최대 10000자)") Integer maxLength
    ) {
        if (!isAllowed(url)) {
            return "보안 정책상 허용되지 않는 URL입니다: " + url +
                   "\n허용 도메인: localhost, 192.168.x.x, ipms.mindpro.co.kr";
        }

        int limit = (maxLength != null) ? Math.min(maxLength, 10000) : 3000;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            String truncated = body.length() > limit ? body.substring(0, limit) + "\n...(이하 생략)" : body;

            log.info("[HttpRequestTool] GET {} → HTTP {}", url, response.statusCode());
            return "HTTP " + response.statusCode() + "\n" + truncated;

        } catch (Exception e) {
            log.error("[HttpRequestTool] GET 실패: {}", url, e);
            return "요청 실패: " + e.getMessage();
        }
    }

    @Tool(description = """
        현재 설정된 프론트엔드 URL을 반환합니다.
        상세 페이지 링크 생성 시 이 값을 prefix로 활용하십시오.
        예) getFrontendBaseUrl() → "http://localhost:4000"
            → 링크: http://localhost:4000/domestic/detail/123
        """)
    public String getFrontendBaseUrl() {
        return frontUrl;
    }

    private boolean isAllowed(String url) {
        if (url == null) return false;
        return ALLOWED_PREFIXES.stream().anyMatch(url::startsWith);
    }
}
