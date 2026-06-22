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
public class KiprisExtensionDetailTool {

    private final RestTemplate restTemplate;
    @Value("${api.kipris.key}") private String kiprisApiKey;
    @Value("${api.kipris.extension-info-url}") private String extensionInfoUrl;

    @Tool(description = """
        KIPRIS에서 특정 특허의 존속기간 연장 등록 정보를 상세 조회합니다. 
        특허권의 기간 연장 여부와 연장 기간을 확인할 때 사용하십시오.
        결과 규격: '연장기간: 값', '등록일: 값' 형태의 텍스트로 반환됩니다.
        """)
    public String kiprisExtensionDetail(String appNo) {
        String cleanAppNo = appNo.replaceAll("[^0-9]", "");
        try {
            String url = UriComponentsBuilder.fromHttpUrl(extensionInfoUrl)
                    .queryParam("applicationNumber", cleanAppNo)
                    .queryParam("accessKey", kiprisApiKey).build().toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return (response != null && !response.contains("code400")) ? response : "연장/분할 정보가 없습니다.";
        } catch (Exception e) {
            log.error("연장정보 조회 실패", e);
            return "조회 오류: " + e.getMessage();
        }
    }
}
