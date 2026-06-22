package kr.co.mindpro.ipms.domain.ai.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KiprisTrademarkClassHistoryTool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.trademark-class-url}") private String trademarkClassUrl;

    @Tool(description = """
        KIPRIS에서 특정 상표의 상품 분류(Nice 분류 등) 이력을 조회합니다. 
        상표가 어느 상품군에 속해 있는지 파악할 때 사용하십시오.
        결과 규격: '상품분류: 값', '지정상품목록: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String kiprisTrademarkClassHistory(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(trademarkClassUrl)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "상표 분류 이력이 없습니다.";
        } catch (Exception e) {
            log.error("상표 분류이력 조회 실패", e);
            return "조회 에러: " + e.getMessage();
        }
    }
}
