package sprint1.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

public class User {

    @Getter
    private UUID id;
    @Getter
    private Long createdAt;
    @Getter
    private Long updatedAt;
    //
    @Getter
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();
        //
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }

    @Override
    public String toString() {
        return "username = " + this.username
            + " email = " + this.email
            + " password = " + this.password;
    }
}

