package com.sprint.mission.discodeit.binaryContent.application.basic;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto request){
        MultipartFile file = request.data();

        try {
            BinaryContent binaryContent = new BinaryContent(file.getOriginalFilename(), file.getSize(),
                    file.getContentType(), file.getBytes());

            binaryContentRepository.save(binaryContent);
            return BinaryContentResponseDto.from(binaryContent);
        } catch (IOException e) {
            throw new NoSuchElementException();
        }


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
