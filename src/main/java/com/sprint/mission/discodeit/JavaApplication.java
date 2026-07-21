package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.UserDto;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // User
        System.out.println(" ===== User =====");
        User me = new User("Parksoomin");
        User u1 = new User("u1");

        // User()
        System.out.println(me);
        System.out.println(u1);

        // User.update()
        u1.update(UserDto.of("Parksoomin"));
        System.out.println(me);
        System.out.println(u1);
        System.out.println("me.equals(u1) = " + me.equals(u1));

        // UserService
        System.out.println(" ===== UserService =====");
        UserService userService = new JCFUserService();

        // create
        System.out.println(" ===== UserService.create() =====");
        System.out.println("userService.create(me) = " + userService.create(me));
        me.update(UserDto.of("SoominPark"));
        System.out.println("userService.create(me) = " + userService.create(me));
        
        // findById
        System.out.println(" ===== UserService.findById() =====");
        System.out.println("userService.findById(me.getId()) = " + userService.findById(me.getId()));
        System.out.println("userService.findById(UUID.randomUUID()) = " + userService.findById(UUID.randomUUID()));

        // update
        System.out.println(" ===== UserService.update() =====");
        userService.update(me.getId(), new User("ParkSoomin"));
        System.out.println(userService.findById(me.getId()));
        userService.update(UUID.randomUUID(), new User("ParkSoomin"));

        // findAll
        System.out.println(" ===== UserService.findAll() =====");
        System.out.println("userService.findAll() = " + userService.findAll());

        // deleteById()
        System.out.println(" ===== UserService.delete() =====");
        userService.deleteById(me.getId());
        System.out.println(userService.findById(me.getId()));

        userService.deleteById(me.getId());
        System.out.println(userService.findById(me.getId()));
    }
}
