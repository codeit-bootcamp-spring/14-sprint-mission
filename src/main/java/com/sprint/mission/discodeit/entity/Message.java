package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {


  private String content;

  //cascade = CascadeType.ALL쓰면 message가 삭제되면 channel가 삭제가 되어서 쓰면 안됨 ( 맞지 않음 )
  //optional = false : channel이 반드시 있어야 한다.
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;

  //cascade = CascadeType.ALL쓰면 message가 삭제되면 user가 삭제가 되어서 쓰면 안됨
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "uuid")
  private User author;

  //cascade = CascadeType.ALL 메세지가 삭제가 되면 파일도 삭제가 되어야 함.
  // orphanRemoval는 부모 엔티티의 컬렉션에서 제거된 자식 엔티티를 DB에서도 자동 삭제
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinTable(
          name = "message_attachments",
          joinColumns = @JoinColumn(name = "message_id"),
          inverseJoinColumns = @JoinColumn(name = "attachment_id")
      )
  private List<BinaryContent> attachments;

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachments = attachments;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
}
