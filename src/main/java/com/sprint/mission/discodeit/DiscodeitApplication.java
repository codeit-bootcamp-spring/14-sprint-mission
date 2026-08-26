package com.sprint.mission.discodeit;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
	info = @Info(
		title = "스프린트 미션5 기본요구사항 API",
		description = "스프린트 미션5 Discodeit API 문서입니다.",
		version = "v1"
	)
)

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);





	}
}
