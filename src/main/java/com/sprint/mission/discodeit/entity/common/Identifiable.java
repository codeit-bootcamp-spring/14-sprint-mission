package com.sprint.mission.discodeit.entity.common;

import java.io.Serializable;
import java.util.UUID;

public interface Identifiable extends Serializable {
    UUID getId();
}
