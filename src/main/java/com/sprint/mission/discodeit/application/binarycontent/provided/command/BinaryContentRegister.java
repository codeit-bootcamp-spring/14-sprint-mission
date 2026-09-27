package com.sprint.mission.discodeit.application.binarycontent.provided.command;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.BinaryContent;
import java.util.List;

public interface BinaryContentRegister {

  List<BinaryContent> register(List<BinaryContentCreateRequest> attachments);
}
