package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.jcf.JCFChennelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserServie;
import com.sprint.mission.discodeit.entity.User;

public class JavaApplication {
    JCFUserServie jcfUserServie = new JCFUserServie();
    JCFChennelService jcfChennelService = new JCFChennelService();
    JCFMessageService jcfMessageService = new JCFMessageService();

    User user1 = new User("yejun","olive1600@naver.com","123414221");
    User user2 = new User("yejun","olive1600@naver.com","123414221");
    User user3 = new User("yejun","olive1600@naver.com","123414221");

}
