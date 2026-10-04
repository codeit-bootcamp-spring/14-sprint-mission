package com.sprint.mission.discodeit.channel.domain;

import com.sprint.mission.discodeit.common.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 10)
  private ChannelType type;
  @Column(name = "name", length = 100)
  private String name;
  @Column(name = "description", length = 500)
  private String description;

  private Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public static Channel createPublic(String name, String description) {
    return new Channel(ChannelType.PUBLIC, name, description);
  }

  public static Channel createPrivate() {
    return new Channel(ChannelType.PRIVATE, null, null);
  }


  public Channel update(String name, String description) {
    if (this.getType() == ChannelType.PRIVATE) {
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
    return this;
  }


  @Override
  public String toString() {
    return "채널 이름 : " + this.name + " 채널 유형 : " + this.type;
  }

}
