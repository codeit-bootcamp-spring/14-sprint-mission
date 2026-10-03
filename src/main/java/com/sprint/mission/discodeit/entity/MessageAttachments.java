package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageAttachments {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "message_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Message message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attachment_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private BinaryContent attachment;

  public MessageAttachments(Message message, BinaryContent attachment) {
    this.message = message;
    this.attachment = attachment;
  }
}
