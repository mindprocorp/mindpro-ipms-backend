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
public class KiprisLegalStatusTool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.legal-status-url}") private String legalStatusUrl;

    @Tool(description = """
        KIPRIS에서 특정 사건의 현재 법적 상태(등록, 거절, 소멸 등)를 조회합니다. 
        사건의 유효 여부를 공신력 있게 확인할 때 호출하십시오.
        결과 규격: '법적상태(status): 값' 형태의 짧은 텍스트로 반환됩니다.
        """)
    public String kiprisLegalStatus(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(legalStatusUrl)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "법적 상태 정보가 없습니다.";
        } catch (Exception e) {
            log.error("법적 상태 조회 실패", e);
            return "조회 오류: " + e.getMessage();
        }
    }
}
