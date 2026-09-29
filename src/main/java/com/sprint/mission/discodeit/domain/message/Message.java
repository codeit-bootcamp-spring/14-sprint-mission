package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {
    @ToString.Include
    private String content;
    @ToString.Include
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User author;
    @ToString.Include
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JoinTable(name = "message_attachments")
    private final List<BinaryContent> attachments = new ArrayList<>();

    public Message(String content, User author, Channel channel, @Nullable List<BinaryContent> attachments) {
        super();
        this.content = content;
        this.author = author;
        this.channel = channel;
        if (Objects.nonNull(attachments)) {
            this.attachments.addAll(attachments);
        }
    }
}
