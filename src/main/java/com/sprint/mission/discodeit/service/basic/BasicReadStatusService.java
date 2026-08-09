package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DuplicateStatus;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto request){

        // 채널 없으면 예외
        if(channelRepository.findById(request.channelId()).isEmpty()){
            throw new NotFoundChannelException();
        }
        // 유저 없으면 예외
        if(userRepository.findById(request.userId()).isEmpty()){
            throw new NotFoundUserException();
        }
        // 채널명, 유저명이 이미 있으면 예외
        if(readStatusRepository.existsByChannelIdAndUserId(request.channelId(), request.userId())){
            throw new DuplicateStatus();
        }

        ReadStatus readStatus = request.toEntity();
        readStatusRepository.save(readStatus);

        return ReadStatusResponseDto.from(request.channelId(), request.userId());
    }

    @Override
    public ReadStatusResponseDto find(UUID id){
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return ReadStatusResponseDto.from(readStatus.getChannelId(), readStatus.getUserId());
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId){
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> ReadStatusResponseDto.from(readStatus.getChannelId(), readStatus.getUserId()))
                .toList();
    }

    @Override
    public ReadStatusResponseDto update(ReadStatusUpdateRequestDto request){

        ReadStatus readStatus = readStatusRepository.findById(request.id()).orElseThrow(NoSuchElementException::new);
        readStatus.updateTime();
        readStatusRepository.update(readStatus);

        return ReadStatusResponseDto.from(readStatus.getChannelId(), readStatus.getUserId());
    }

    @Override
    public void delete(UUID id){
        readStatusRepository.findById(id).orElseThrow(NoSuchElementException::new); // 없으면 예외 띄우기용도
        readStatusRepository.deleteById(id);
    }

}
