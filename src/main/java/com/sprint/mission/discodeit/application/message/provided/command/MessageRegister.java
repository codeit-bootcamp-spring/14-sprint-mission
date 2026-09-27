package com.sprint.mission.discodeit.application.message.provided.command;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.application.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import java.util.List;

public interface MessageRegister {

  MessageDto register(MessageCreateRequest request, List<BinaryContentCreateRequest> attachments);
}
