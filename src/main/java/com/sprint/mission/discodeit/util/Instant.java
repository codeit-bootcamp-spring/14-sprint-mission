package com.sprint.mission.discodeit.util;

public class Instant {
    public static Long now() {
        return java.time.Instant.now().getEpochSecond();
    }
}
