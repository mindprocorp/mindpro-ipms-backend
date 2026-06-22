package kr.co.mindpro.ipms.domain.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // KIPRIS의 망가진 Content-Type 헤더를 강제로 수정하는 인터셉터
        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            // 헤더에서 'text/xml; =;charset=UTF-8' 같은 쓰레기 값을 'text/xml;charset=UTF-8'로 치환
            String contentType = response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            if (contentType != null && contentType.contains("; =;")) {
                // 이 부분이 핵심입니다!
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, contentType.replace("; =;", ";"));
            }
            return response;
        });

        return restTemplate;
    }
}