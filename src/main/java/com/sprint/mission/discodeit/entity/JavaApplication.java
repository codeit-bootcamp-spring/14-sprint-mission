package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args){


        UserService service = new JCFUserService();

        //등록

        User user1 = new User("홍길동",20);
        service.create(user1);

        User user2 = new User("이현석",25);
        service.create(user2);

        User user3 = new User("지킴이", 23);
        service.create(user3);

        //수정
        user3.update("집감이",24);
        service.update(user3);

        //수정된 데이터 조회
        System.out.print(service.find(user3.getId()).getName());
        System.out.println(service.find(user3.getId()).getAge());

        //삭제
        service.delete(user2.getId());
        User found = service.find(user2.getId());

        if(found == null){
            System.out.println("삭제되었습니다.");
        }
        else{
            System.out.println("삭제 실패");
        }



        /*MessageService service1 = new JCFMessageService();

        Message message1 = new Message("택배기사",7);
        service1.create(message1);



        ChannelService service2 = new JCFChannelService();

        Channel channel1 = new Channel ("MBC",8);
        service2.create(channel1);
        */


       //조회(단건)
        System.out.println(service.find(user1.getId()).getName());

        //조회(다건)
        for (User user : service.findAll()) {
            System.out.println(
                "이름 : " + user.getName() +
                " 나이 : " + user.getAge()
            );
        }



        /*System.out.println(service1.find(message1.getId()).getName());
        System.out.println(service1.find(message1.getId()).getCount());
        System.out.println(service2.find(channel1.getId()).getName());
        System.out.println(service2.find(channel1.getId()).getChannelNum());


         */
    }

}
