package kr.co.mindpro.ipms;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableScheduling
@org.springframework.scheduling.annotation.EnableAsync
public class MindproIpmsMindapiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MindproIpmsMindapiApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(MindproIpmsMindapiApplication.class, args);
	}

}
