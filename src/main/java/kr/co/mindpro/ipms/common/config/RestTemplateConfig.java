package kr.co.mindpro.ipms.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 외부 API 통신을 위한 RestTemplate 설정
 *
 * @author   : mindpro
 * @since    : 2026. 1. 12.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
