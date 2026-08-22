package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

/** 이름·설명이 없다. 참여자 목록만으로 만드는 DM/그룹DM 개념. */
public record PrivateChannelCreateRequest(
        List<UUID> participantIds
) {
}
