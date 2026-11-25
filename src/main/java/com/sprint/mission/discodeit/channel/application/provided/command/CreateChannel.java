package com.sprint.mission.discodeit.channel.application.provided.command;

import java.util.List;
import java.util.UUID;

public sealed interface CreateChannel permits CreateChannel.Public, CreateChannel.Private {

  record Public(
      String name,
      String description
  ) implements CreateChannel {

  }


  record Private(List<UUID> participantsIds) implements CreateChannel {

    public Private {
      participantsIds = List.copyOf(participantsIds);
    }
  }

}
