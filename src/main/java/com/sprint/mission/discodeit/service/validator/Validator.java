package com.sprint.mission.discodeit.service.validator;

import java.util.List;
import java.util.UUID;

public interface Validator {
    void validate(UUID id);

    void validateAll(List<UUID> ids);
}

