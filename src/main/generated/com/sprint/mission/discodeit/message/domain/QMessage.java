package com.sprint.mission.discodeit.message.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessage extends EntityPathBase<Message> {

    private static final long serialVersionUID = -904935019L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessage message1 = new QMessage("message1");

    public final com.sprint.mission.discodeit.common.entity.QBaseUpdatableEntity _super = new com.sprint.mission.discodeit.common.entity.QBaseUpdatableEntity(this);

    public final ListPath<com.sprint.mission.discodeit.binaryContent.domain.BinaryContent, com.sprint.mission.discodeit.binaryContent.domain.QBinaryContent> attachments = this.<com.sprint.mission.discodeit.binaryContent.domain.BinaryContent, com.sprint.mission.discodeit.binaryContent.domain.QBinaryContent>createList("attachments", com.sprint.mission.discodeit.binaryContent.domain.BinaryContent.class, com.sprint.mission.discodeit.binaryContent.domain.QBinaryContent.class, PathInits.DIRECT2);

    public final com.sprint.mission.discodeit.user.domain.QUser author;

    public final com.sprint.mission.discodeit.channel.domain.QChannel channel;

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QMessage(String variable) {
        this(Message.class, forVariable(variable), INITS);
    }

    public QMessage(Path<? extends Message> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessage(PathMetadata metadata, PathInits inits) {
        this(Message.class, metadata, inits);
    }

    public QMessage(Class<? extends Message> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.sprint.mission.discodeit.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.channel = inits.isInitialized("channel") ? new com.sprint.mission.discodeit.channel.domain.QChannel(forProperty("channel")) : null;
    }

}

