package sprint1.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

public class Channel {

    @Getter
    private UUID id;
    @Getter
    private Long createdAt;
    @Getter
    private Long updatedAt;
    //
    @Getter
    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        //
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public void update(ChannelType newType) {
        boolean anyValueUpdated = false;
        if (newType != null && !newType.equals(this.type)) {
            this.type = newType;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }

    }

    public String toString() {
        return " PUBLIC/PRIVATE중에 = " + this.type
            + " 채널이름은: " + this.name
            + " 채널소개명은: " + this.description;
    }
}
