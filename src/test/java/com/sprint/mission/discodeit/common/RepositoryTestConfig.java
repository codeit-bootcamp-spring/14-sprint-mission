package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapperImpl;
import com.sprint.mission.discodeit.common.config.QuerydslConfig;
import com.sprint.mission.discodeit.user.mapper.UserMapperImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @DataJpaTest는 모든 Repository를 띄우는데, ChannelRepositoryImpl(QueryDSL)이
 * JPAQueryFactory와 ChannelMapper를 필요로 해서 슬라이스에 직접 등록한다.
 */
@TestConfiguration
@Import({QuerydslConfig.class, ChannelMapperImpl.class, UserMapperImpl.class, BinaryContentMapperImpl.class})
public class RepositoryTestConfig {
}
