package com.sprint.mission.discodeit.domain.service.message;

import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.repository.MessageRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
    삭제 0개를 실패로 바라본다면 -> 예외 던지기
    가능한 경우라고 본다면 -> 그대로 진행
 */
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(Message message, List<UUID> imageList) {

        if(Objects.nonNull(imageList)){
            message.updateMessageImagesFiled(imageList);
        }

        return messageRepository.saveEntity(message);
    }

    @Override
    public Message findMessageById(UUID messageId) {

        return messageRepository.findById(messageId)
            .orElseThrow(() -> new CustomException(CustomErrorCode.MESSAGE_NOT_FOUND));
    }

    @Override
    public void deleteMessage(UUID messageId) {

        this.findMessageById(messageId);

        messageRepository.deleteEntity(messageId);
    }

    @Override
    public List<Message> findAllMessageByChannelId(UUID channelId) {

        return messageRepository.findAllMessageByChannelId(channelId);
    }

    @Override
    public Optional<Message> findLastMessageByChannelId(UUID channelId) {

        /*
            옵셔널로 다시 던지는 이유 - 아무런 메시지가 없는 경우 널이기 때문에
            널이 들어있을 수도 있는 상황에서 바로 컨트롤러로 넘어가는게 아닌 s1 계층으로 던지기 때문에
            이 메서드의 결과는 없을 수 있음을 알리기 위함
         */
        return messageRepository.findLastMessageByChannelId(channelId);
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        //삭제가 되지 않아도 ok - 개체 없어도 ok

        messageRepository.deleteMessageByChannelId(channelId);
    }

    @Override
    public Message updateMessageContent(UUID messageId, String content) {

        Message message = this.findMessageById(messageId);
        message.updateContent(content);

        //맵구조라 세이브로 호출
        return messageRepository.saveEntity(message);
    }
}
