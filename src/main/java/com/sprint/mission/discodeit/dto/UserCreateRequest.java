package com.sprint.mission.discodeit.dto;

/**
 * 유저 등록에 필요한 값들.
 * 나열하면 같은 타입이 이어져 순서를 바꿔 넣어도 컴파일된다. 묶으면 그 실수가 사라진다.
 */
public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}
