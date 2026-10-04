package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 테스트 시에는 주석 처리 필요
//@EnableJpaAuditing
public class DiscodeitApplication {

    public static void main(String[] args) {

        SpringApplication.run(DiscodeitApplication.class, args);
    }
}