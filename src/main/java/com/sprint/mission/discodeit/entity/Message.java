package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  //
  private String content;
  //
  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Channel channel;
  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private User author;

  public Message(String content, Channel channel, User author) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
