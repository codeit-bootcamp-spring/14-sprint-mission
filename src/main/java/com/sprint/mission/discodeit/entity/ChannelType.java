package com.sprint.mission.discodeit.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level =  AccessLevel.PRIVATE)
public enum ChannelType {
  PUBLIC("공개"),
  PRIVATE("비공개");

  String description;
}
