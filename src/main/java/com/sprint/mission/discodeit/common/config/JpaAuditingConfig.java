package com.sprint.mission.discodeit.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 메인 클래스에 두면 @WebMvcTest 같은 슬라이스 테스트에서도 JPA Auditing이 켜져서 분리
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
