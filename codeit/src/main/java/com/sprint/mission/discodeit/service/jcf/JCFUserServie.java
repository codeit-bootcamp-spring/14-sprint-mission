package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.entity.User;


import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserServie implements UserService {

    private Map<UUID, User> userMap = new HashMap<>();

    @Override
    public void create(User user){
        if(userMap.containsKey(user.getId())){
            throw new RuntimeException("생성하시려는 유저가 이미 있습니다." + user.getId());
        }
        userMap.put(user.getId(),user);
        System.out.println("유저를 생성했습니다.");
    }

    @Override
    public User read(User user) {
        if(!userMap.containsKey(user.getId())){
            throw new RuntimeException("읽으시려는 유저가 없습니다."+ user.getId());
        }
        User userRead = userMap.get(user.getId());
        String userName = userRead.getName();
        System.out.println("읽으시려는 유저 이름:"+ userName);
        return user;
    }

    @Override
    public void update(User user,String name, String password, String email){
        if(!userMap.containsKey(user.getId())) {
            throw new RuntimeException("업데이트 할려는 유저가 없습니다");
        }
        user.update(name,email,password);
    }

    @Override
    public void delete(User user){
        if(!userMap.containsKey(user.getId())){
            throw new RuntimeException("삭제하려는 유저가 없습니다");
        }
        userMap.remove(user.getId());
    }


}
