package com.sprint.mission.discodeit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// test 프로파일(H2)로 띄워서 로컬 PostgreSQL 없이도 실행되게 함
@SpringBootTest
@ActiveProfiles("test")
class DiscodeitApplicationTests {

	@Test
	void contextLoads() {
	}

}
