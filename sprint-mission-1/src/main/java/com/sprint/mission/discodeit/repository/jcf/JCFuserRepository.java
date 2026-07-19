package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enumType.userRole;
import com.sprint.mission.discodeit.repository.userRepository;

import java.util.ArrayList;
import java.util.List;


public abstract class JCFuserRepository implements userRepository {
    protected final List<User> users = new ArrayList<>();

    @Override
    public User userCreate(String userName, String role) {
        boolean userExists = true;
        boolean roleExists = false;
        for (User each:users){
            if (each.getUserName().equals(userName)){
                System.out.println("이미 생성된 사용자의 이름입니다: "+userName);
                userExists = false;
            }
        }
        for (userRole userRole:userRole.values()){
            if (role.equals(userRole.getRoleName())){
                roleExists = true;
                break;
            }
        }
        if (userExists && roleExists){
            System.out.println("사용자 생성이 완료되었습니다: "+userName);
            users.add(new User(userName, role));
            return new User(userName, role);
        } else if (!roleExists){
            System.out.println("존재하지 않는 역할입니다: "+role);
        }
        return null;
    }

    @Override
    public User findByUser(String userName) {
        for (User user:users){
            if (user.getUserName().equals(userName)){
                return user;
            }
        }
        System.out.println("찾는 사용자의 이름이 없습니다: "+userName);
        return null;
    }

    @Override
    public void userUpdate(String userName, String updateUserName) {
        User user = findByUser(userName);
        if (user != null){
            System.out.println(userName+" 사용자의 이름을 "+updateUserName+"로 수정했습니다.");
            user.updateName(updateUserName);
        }
    }

    @Override
    public void userDelete(String userName) {
        User user = findByUser(userName);
        if (user != null){
            System.out.println(userName+" 사용자를 삭제했습니다.");
            users.remove(user);
        }
    }
}
