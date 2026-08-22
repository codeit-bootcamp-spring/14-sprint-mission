package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.binaryContent.application.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicBinaryContentServiceTest {

    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp(){
        binaryContentService = new BasicBinaryContentService(
                new JCFBinaryContentRepository()
        );
    }

    @Test
    void 생성하면_조회_할_수_있다(){
        BinaryContentResponseDto created = binaryContentService.create(new BinaryContentCreateRequestDto(new byte[]{1, 2, 3}));
        BinaryContentResponseDto found = binaryContentService.find(created.id());
        assertEquals(created.data(), found.data());
    }

    @Test
    void 같은_아이디로_여러개_생성하면_리스트를_조회_할_수_있다(){
        List<BinaryContentResponseDto> created = new ArrayList<>();
        created.add(binaryContentService.create(new BinaryContentCreateRequestDto(new byte[]{1, 2, 3})));
        created.add(binaryContentService.create(new BinaryContentCreateRequestDto(new byte[]{4, 5, 6})));
        created.add(binaryContentService.create(new BinaryContentCreateRequestDto(new byte[]{7, 8, 9})));

        List<UUID> ids = created.stream()
                .map(BinaryContentResponseDto::id)
                .toList();

        List<BinaryContentResponseDto> found = binaryContentService.findAllByIdIn(ids);

        List<UUID> foundIds = found.stream()
                .map(BinaryContentResponseDto::id)
                .toList();


        assertEquals(created.size(), found.size());
        assertTrue(foundIds.containsAll(ids));



    }

    @Test
    void 생성하면_삭제_할_수_있다(){
        BinaryContentResponseDto created = binaryContentService.create(new BinaryContentCreateRequestDto(new byte[]{1, 2, 3}));
        binaryContentService.delete(created.id());
        assertThrows(NoSuchElementException.class, () -> binaryContentService.find(created.id()));
    }

}



