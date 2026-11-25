package com.sprint.mission.discodeit.user.service;

import java.util.UUID;

// 접속 여부 판단은 인증(auth) 모듈의 세션 정보로 한다.
// user가 auth를 직접 의존하면 순환이 생기므로 인터페이스만 user에 두고 구현은 auth에 둔다
public interface UserOnlineChecker {

    boolean isOnline(UUID userId);
}
