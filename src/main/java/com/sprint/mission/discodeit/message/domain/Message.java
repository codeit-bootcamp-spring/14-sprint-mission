package com.sprint.mission.discodeit.message.domain;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor
public class Message extends BaseUpdatableEntity {

    @Column(name = "content")
    private String message;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<BinaryContent> attachments;

    private Message(List<BinaryContent> attachments, String message, Channel channel, User author) {
        super();
        if(attachments == null){
            attachments = new ArrayList<>();
        }
        this.attachments = attachments;
        this.message = message;
        this.channel = channel;
        this.author = author;
    }

    public static Message create(List<BinaryContent> attachments, String message, Channel channel, User author){
        return new Message(attachments, message, channel, author);
    }

    public void update(String message){
        if(message != null) this.message = message;
        this.updateUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", channel=" + channel +
                ", User=" + author +
                '}';
    }
}
