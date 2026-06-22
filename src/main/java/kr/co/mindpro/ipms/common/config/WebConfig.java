package kr.co.mindpro.ipms.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring MVC 공통 설정
 * Swagger UI에서 multipart/form-data 전송 시 JSON 파트가 
 * application/octet-stream으로 전송되는 이슈를 해결합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // MappingJackson2HttpMessageConverter를 찾아 octet-stream 타입을 지원 리스트에 추가
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                List<MediaType> supportedTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                supportedTypes.add(MediaType.valueOf("application/octet-stream"));
                jacksonConverter.setSupportedMediaTypes(supportedTypes);
            }
        }
    }
}
