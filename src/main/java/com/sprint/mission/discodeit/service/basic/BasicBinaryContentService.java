package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto request){

        BinaryContent binaryContent = request.toEntity();

        binaryContentRepository.save(binaryContent);

        return BinaryContentResponseDto.from(binaryContent);

    }

    @Override
    public BinaryContentResponseDto find(UUID id){
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids){
        List<BinaryContentResponseDto> response = binaryContentRepository.findAllByIdIn(ids)
                .stream()
                .map(BinaryContentResponseDto::from)
                .toList();

        // 비어 있으면 예외 발생
        if(response.isEmpty()){
            throw new NoSuchElementException();
        }

        return response;
    }

    @Override
    public void delete(UUID id){
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(NoSuchElementException::new);
        binaryContentRepository.deleteById(binaryContent.getId());
    }


}
