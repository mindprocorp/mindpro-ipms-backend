package kr.co.mindpro.ipms.common.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. SecurityScheme 이름 정의
        String securitySchemeName = "bearerAuth";

        // 2. 인증 요구사항 정의 (모든 API에 자물쇠 아이콘 표시)
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        // 3. SecurityScheme 설정 (Bearer JWT 방식)
        Components components = new Components().addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // 4. API 문서 정보 및 최종 OpenAPI 객체 반환
        return new OpenAPI()
                .info(new Info()
                        .title("MINDPRO-IPMS API Document")
                        .description("MINDPRO-IPMS 프로젝트의 API 명세서입니다.")
                        .version("v1.0.0"))
                .servers(List.of(new Server().url("/").description("Default Server URL")))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("00_전체") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
                .packagesToScan("kr.co.mindpro.ipms")
                .build();
    }    
    
    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("01_공통") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
//                .packagesToScan("kr.co.mindpro.ipms")
                .pathsToMatch("/api/common/**", "/api/code/**", "/api/BizInfo/**", "/api/v1/common/**", "/api/v1/files/**", "/api/searchcindition/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi aiApi() {
        return GroupedOpenApi.builder()
                .group("02_AI") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
//                .packagesToScan("kr.co.mindpro.ipms")
                .pathsToMatch("/api/ai/**")
                .build();
    }    
    
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("03_인증(사용자)") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
//                .packagesToScan("kr.co.mindpro.ipms")
                .pathsToMatch("/api/auth/**", "/api/users/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi projectApi() {
        return GroupedOpenApi.builder()
                .group("04_업무API") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
//                .packagesToScan("kr.co.mindpro.ipms")
                .pathsToMatch("/api/customer/**", "/api/domesticApp/**", "/api/duedate/**", "/api/conflict/**", "/api/oversea/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi tapApi() {
        return GroupedOpenApi.builder()
                .group("05_업무_탭 API") // 그룹 이름 (Swagger UI 우측 상단 선택 메뉴)
                .pathsToMatch("/**") // 모든 경로 포함
                // TODO: 실제 컨트롤러가 있는 패키지 경로로 수정하세요
//                .packagesToScan("kr.co.mindpro.ipms")
                .pathsToMatch("/api/gracePeriod/**", "/api/Memo/**", "/api/cost/**", "/api/fileList/**", "/api/rnd/**", "/api/preference/**", "/api/progress/**", "/api/locarno/**")
                .build();
    }       
}
