package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends Basic {

    private String content;
    private UUID channelId; // 메시지는 여러채널에 있을 수 있다.
    private UUID authorId; // 누가 작성한 메시지인가?
    private List<UUID> attachmentIds; // 메시지에 첨부파일(BinaryContent)를 참조.


    private Message(
            UUID id,
            String content,
            UUID channelId,
            UUID authorId,
            List<UUID> attachmentIds
    ) {
        super(id);
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds == null
                ? List.of()
                : List.copyOf(attachmentIds);
    }

    public static Message create(
            UUID id,
            String content,
            UUID channelId,
            UUID authorId,
            List<UUID> attachmentIds
    ) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("메시지 내용은 필수입니다.");
        }
        if (channelId == null || authorId == null) {
            throw new IllegalArgumentException("채널과 작성자는 필수입니다.");
        }

        return new Message(id, content, channelId, authorId, attachmentIds);
    }

    public void updateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("메시지 내용은 필수입니다.");
        }

        this.content = content;
        this.updatedAt = Instant.now();
    }

    public void updateAttachments(List<UUID> attachmentIds) {
        this.attachmentIds = attachmentIds == null
                ? List.of()
                : List.copyOf(attachmentIds);
        this.updatedAt = Instant.now();
    }


}
