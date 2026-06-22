package kr.co.mindpro.ipms.common.nts;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.mindpro.ipms.common.nts.dto.NtsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 국세청 API 실시간 통신 클라이언트
 * 
 * @author  : mindpro
 * @since   : 2026. 1. 12.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NtsApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.nts.key}")
    private String serviceKey;

    // 국세청 사업자등록 상태조회 API URL (v1.1)
//    private static final String STATUS_URL = "api.odcloud.kr";
    private static final String STATUS_URL = "https://api.odcloud.kr/api/nts-businessman/v1/status";

    /**
     * 국세청 상태조회 API 호출 (POST)
     * @param businessNumber 사업자번호
     * @return API 응답 Raw 데이터 (Map)
     */
    public Map<String, Object> fetchStatus(String businessNumber) {
        String cleanBno = businessNumber.replaceAll("-", "");

        try {
            // !!! Decoding 키의 특수문자(+, =) 보존을 위해 직접 문자열 결합 후 URI 객체 생성
            // RestTemplate은 URI 객체가 인자로 들어오면 자동으로 인코딩을 수행하지 않습니다.
        		String safeKey = serviceKey.replace("+", "%2B");
            URI uri = new URI(STATUS_URL + "?serviceKey=" + safeKey);

            log.info("### [NTS API Request] URI: {}", uri);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            // 명세서 AJAX 예제의 JSON.stringify(data)와 동일한 역할
            NtsDto.StatusRequest body = new NtsDto.StatusRequest(List.of(cleanBno));
            HttpEntity<NtsDto.StatusRequest> entity = new HttpEntity<>(body, headers);

            return restTemplate.postForObject(uri, entity, Map.class);

        } catch (Exception e) {
            log.error("### [NTS API Error] Message: {}", e.getMessage());
            throw new RuntimeException("국세청 API 통신 중 오류가 발생했습니다.");
        }
    }    
}
