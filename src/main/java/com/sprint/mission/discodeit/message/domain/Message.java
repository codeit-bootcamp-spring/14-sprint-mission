package com.sprint.mission.discodeit.message.domain;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;
  @JoinColumn(name = "channel_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Channel channel;
  @JoinColumn(name = "author_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User author;
  @JoinTable(name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id"))
  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<BinaryContent> attachments;

  private Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    if (attachments == null) {
      this.attachments = new ArrayList<>();
    }
    if (attachments != null) {
      this.attachments = new ArrayList<>(attachments);
    }
  }

  public static Message create(String content, Channel channel, User author,
      List<BinaryContent> attachments) {
    return new Message(content, channel, author, attachments);
  }


  public Message update(String updateContent) {
    this.content = updateContent;
    return this;
  }

  @Override
  public String toString() {
    return "내용 : " + this.content;
  }

}
