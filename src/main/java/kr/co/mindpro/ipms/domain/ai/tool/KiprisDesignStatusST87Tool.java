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
public class KiprisDesignStatusST87Tool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.design-st87-url}") private String designSt87Url;

    @Tool(description = """
        KIPRIS에서 특정 디자인의 ST.87 표준 법적 상태 정보를 상세 조회합니다. 
        디자인권의 국제 표준 권리 형태를 확인할 때 사용하십시오.
        결과 규격: 'ST87상태: 값', '변경일: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String kiprisDesignStatusST87(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(designSt87Url)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "ST.87 디자인 상태 정보가 없습니다.";
        } catch (Exception e) {
            log.error("디자인 ST87 조회 실패", e);
            return "조회 에러: " + e.getMessage();
        }
    }
}
