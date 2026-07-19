package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.repository.chatRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFchatRepository;

public class JCFchatService extends JCFchatRepository implements chatRepository {

    @Override
    public void chatHistory() {
        int start=Math.max(0, messages.size()-10);
        for (int i=start; i<messages.size(); i++){
            System.out.println(messages.get(i).getMessage()+
                    " | 유저: "+messages.get(i).getUser().getUserName()+
                    "("+messages.get(i).getUser().getRole()+")"+
                    " " +messages.get(i).getCreateAt());
        }
    }
}
