package sprint1.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel {

    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    //
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

    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
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
