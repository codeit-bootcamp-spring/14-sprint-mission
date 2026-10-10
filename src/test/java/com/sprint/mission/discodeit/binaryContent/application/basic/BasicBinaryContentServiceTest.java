package com.sprint.mission.discodeit.binaryContent.application.basic;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.common.exception.BinaryContentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BasicBinaryContentServiceTest {

    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private BinaryContentMapper binaryContentMapper;

    @InjectMocks
    private BasicBinaryContentService binaryContentService;

    private BinaryContent createBinaryContent(UUID id) {
        BinaryContent binaryContent = new BinaryContent("a.png", 3L, "image/png");
        ReflectionTestUtils.setField(binaryContent, "id", id);
        return binaryContent;
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("성공 - 메타데이터를 저장하고 파일을 저장소에 쓴다")
        void create_success() {
            // given
            MockMultipartFile file = new MockMultipartFile("data", "a.png", "image/png", new byte[]{1, 2, 3});
            BinaryContentDto expected = new BinaryContentDto(UUID.randomUUID(), "a.png", 3L, "image/png");
            given(binaryContentMapper.toDto(any(BinaryContent.class))).willReturn(expected);

            // when
            BinaryContentDto result = binaryContentService.create(new BinaryContentCreateRequestDto(file));

            // then
            assertThat(result).isEqualTo(expected);
            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().put(any(), eq(new byte[]{1, 2, 3}));
        }

        @Test
        @DisplayName("실패 - 파일을 읽지 못하면 UncheckedIOException")
        void create_ioFail() throws IOException {
            // given
            MultipartFile file = mock(MultipartFile.class);
            given(file.getOriginalFilename()).willReturn("a.png");
            given(file.getSize()).willReturn(3L);
            given(file.getContentType()).willReturn("image/png");
            given(file.getBytes()).willThrow(new IOException("디스크 오류"));

            // when & then
            assertThatThrownBy(() -> binaryContentService.create(new BinaryContentCreateRequestDto(file)))
                    .isInstanceOf(UncheckedIOException.class);
            then(binaryContentStorage).shouldHaveNoInteractions();
            then(binaryContentMapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("find")
    class Find {

        @Test
        @DisplayName("성공 - id로 파일 정보를 조회한다")
        void find_success() {
            UUID id = UUID.randomUUID();
            BinaryContent binaryContent = createBinaryContent(id);
            BinaryContentDto expected = new BinaryContentDto(id, "a.png", 3L, "image/png");
            given(binaryContentRepository.findById(id)).willReturn(Optional.of(binaryContent));
            given(binaryContentMapper.toDto(binaryContent)).willReturn(expected);

            assertThat(binaryContentService.find(id)).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 - 없는 id면 BinaryContentNotFoundException")
        void find_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(binaryContentRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> binaryContentService.find(id))
                    .isInstanceOf(BinaryContentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findAllByIdIn")
    class FindAllByIdIn {

        @Test
        @DisplayName("성공 - 여러 id의 파일 정보를 조회한다")
        void findAll_success() {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();
            given(binaryContentRepository.findAllByIdIn(List.of(id1, id2)))
                    .willReturn(List.of(createBinaryContent(id1), createBinaryContent(id2)));
            given(binaryContentMapper.toDto(any(BinaryContent.class)))
                    .willReturn(new BinaryContentDto(UUID.randomUUID(), "a.png", 3L, "image/png"));

            assertThat(binaryContentService.findAllByIdIn(List.of(id1, id2))).hasSize(2);
        }

        @Test
        @DisplayName("실패 - 하나도 없으면 BinaryContentNotFoundException")
        void findAll_empty_fail() {
            List<UUID> ids = List.of(UUID.randomUUID());
            given(binaryContentRepository.findAllByIdIn(ids)).willReturn(List.of());

            assertThatThrownBy(() -> binaryContentService.findAllByIdIn(ids))
                    .isInstanceOf(BinaryContentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 파일 정보를 삭제한다")
        void delete_success() {
            UUID id = UUID.randomUUID();
            given(binaryContentRepository.findById(id)).willReturn(Optional.of(createBinaryContent(id)));

            binaryContentService.delete(id);

            then(binaryContentRepository).should().deleteById(id);
        }

        @Test
        @DisplayName("실패 - 없는 id면 BinaryContentNotFoundException, 삭제하지 않는다")
        void delete_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(binaryContentRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> binaryContentService.delete(id))
                    .isInstanceOf(BinaryContentNotFoundException.class);
            then(binaryContentRepository).should(never()).deleteById(any());
        }
    }
}
