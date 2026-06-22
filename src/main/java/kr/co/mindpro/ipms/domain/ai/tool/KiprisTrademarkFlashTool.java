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
public class KiprisTrademarkFlashTool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.trademark-flash-url}") private String trademarkFlashUrl;

    @Tool(description = """
        KIPRIS에서 특정 상표의 속보(Flash) 정보를 조회합니다. 
        가장 최근에 공고된 상표의 권리 정보를 빠르게 파악할 때 사용하십시오.
        결과 규격: '상표명(title): 값', '지정상품: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String kiprisTrademarkFlash(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(trademarkFlashUrl)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "상표 속보 정보가 없습니다.";
        } catch (Exception e) {
            log.error("상표 속보 조회 실패", e);
            return "조회 에러: " + e.getMessage();
        }
    }
}
