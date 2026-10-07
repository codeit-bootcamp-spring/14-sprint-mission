package com.sprint.mission.discodeit.common.exception.user;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import java.util.Map;

public class UserException extends DiscodeitException {

    /* 왜이렇게 보일러 플레이트를 만드는지 질문하기
       이전에는 그냥 CustomException 하나만두고 Custom으로 다던져서 클래스파일이 많지 않았음
     */
    public UserException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
