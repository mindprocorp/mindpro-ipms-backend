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
public class KiprisPatentAdminHistoryTool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.patent-admin-url}") private String patentAdminUrl;

    @Tool(description = """
        KIPRIS에서 특정 특허의 행정 절차 이력(심사, 등록 등)을 상세 조회합니다. 
        특허청의 실제 처리 단계를 확인할 때 사용하십시오.
        결과 규격: '항목(필드명): 값' 형태의 타임라인 텍스트로 반환됩니다.
        """)
    public String kiprisPatentAdminHistory(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(patentAdminUrl)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "진행 이력 결과가 없습니다.";
        } catch (Exception e) {
            log.error("특허 행정이력 조회 실패", e);
            return "조회 중 오류: " + e.getMessage();
        }
    }
}
