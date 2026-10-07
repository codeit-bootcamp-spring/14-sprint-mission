package com.sprint.mission.discodeit.message.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import java.util.List;

public interface MessageRegister {

  MessageDto register(MessageCreateRequest request, List<BinaryContentCreateRequest> attachments);
}
