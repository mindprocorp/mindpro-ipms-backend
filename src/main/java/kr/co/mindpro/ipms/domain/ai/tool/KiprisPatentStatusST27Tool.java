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
public class KiprisPatentStatusST27Tool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.patent-st27-url}") private String patentSt27Url;

    @Tool(description = """
        KIPRIS에서 특정 특허의 ST.27 표준 법적 상태 정보를 상세 조회합니다. 
        국제 표준 규격에 의한 정밀한 권리 상태를 확인할 때 사용하십시오.
        결과 규격: 'ST27상태: 값', '변경일: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String kiprisPatentStatusST27(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(patentSt27Url)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "ST.27 상세 상태 정보가 없습니다.";
        } catch (Exception e) {
            log.error("특허 ST27 조회 실패", e);
            return "조회 에러: " + e.getMessage();
        }
    }
}
