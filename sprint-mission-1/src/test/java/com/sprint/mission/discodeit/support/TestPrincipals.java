package com.sprint.mission.discodeit.support;

import com.sprint.mission.discodeit.auth.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;

// @WithMockUser의 principal은 Spring User라 authentication.principal.userDto를 쓰는 SpEL을 통과할 수 없다.
// 실제 로그인과 같은 DiscodeitUserDetails를 만들어 .with(user(...))로 넣는다
public final class TestPrincipals {

    private TestPrincipals() {
    }

    public static DiscodeitUserDetails of(User user) {
        UserDto dto = new UserDto(user.getId(), user.getUserName(), user.getEmail(), null, false,
            user.getRole());
        return new DiscodeitUserDetails(dto, user.getPassword());
    }
}
