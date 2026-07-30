package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Chennel;
import com.sprint.mission.discodeit.service.ChennelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChennelService implements ChennelService {
    private Map<UUID, Chennel> chennelMap = new HashMap<>();

    @Override
    public void create(Chennel chennel){
        if(chennelMap.containsKey(chennel.getId())){
            throw new RuntimeException("생성하시려는 유저가 이미 있습니다." + chennel.getId());
        }
        chennelMap.put(chennel.getId(),chennel);
        System.out.println("유저를 생성했습니다.");
    }

    @Override
    public Chennel read(Chennel chennel) {
        if(!chennelMap.containsKey(chennel.getId())){
            throw new RuntimeException("읽으시려는 유저가 없습니다."+ chennel.getId());
        }
        return chennelMap.get(chennel);
    }

    @Override
    public void ChennelUpdate(Chennel chennel,String channelName, int channelNumber){
        if(!chennelMap.containsKey(chennel.getId())) {
            throw new RuntimeException("업데이트 할려는 유저가 없습니다");
        }
        chennel.update(channelName, channelNumber);
    }

    @Override
    public void delete(Chennel chennel){
        if(!chennelMap.containsKey(chennel.getId())){
            throw new RuntimeException("삭제하려는 유저가 없습니다");
        }
        chennelMap.remove(chennel.getId());
    }
}
