package com.sprint.mission.discodeit.binarycontent.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.List;

public interface BinaryContentRegister {

  List<BinaryContent> register(List<BinaryContentCreateRequest> attachments);
}
