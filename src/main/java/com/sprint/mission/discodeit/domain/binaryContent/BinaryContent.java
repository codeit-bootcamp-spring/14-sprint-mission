package com.sprint.mission.discodeit.domain.binaryContent;

import com.sprint.mission.discodeit.domain.common.BasicEntity;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
 *
 */
@Getter
public class BinaryContent extends BasicEntity {
    /*
    User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.
     */
    private final MultipartFile content;

    public BinaryContent(MultipartFile content) {
        super();
        this.content = content;
    }
}
