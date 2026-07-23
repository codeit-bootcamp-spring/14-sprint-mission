package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {
		ServiceFactory serviceFactory = new ServiceFactory();
		UserService userService = serviceFactory.getUserService();
		ChannelService channelService = serviceFactory.getChannelService();
		MessageService messageService = serviceFactory.getMessageService();

		//유저 3명 만듬
		User kim = userService.createUser("kim@gmail", "김어진");
		User lee = userService.createUser("lee@gmail", "리어진");
		User park = userService.createUser("park@gmail", "박어진");
		User han = userService.createUser("han@gmail", "한어진");
		User gong = userService.createUser("gong@gmail", "공어진");
		User nam = userService.createUser("nam@gmail", "남어진");

		//유저 한명 조회
		System.out.println("유저 조회 " + userService.readUser(kim.getId()).getName());

		//전체 유저 조회
		List<User> allUser = userService.readAllUser();
		System.out.println(" === 유저 목록 === ");
		for (User each : allUser) {
			System.out.println(each.getName());
		}
		System.out.println();

		//유저 업데이트 조회
		System.out.print("수정전: ");
		System.out.println(kim);

		userService.updateUser(kim.getId(), "김어진이");
		System.out.print("수정후: ");
		System.out.println(kim);
		System.out.println();

		//유저 삭제
		System.out.println(" === 유저 목록 === ");
		userService.deleteUser(kim.getId());
		for (User each : allUser) {
			System.out.println(each);
		}
		System.out.println();
		System.out.println(" === User 테스트 완료 === ");
		System.out.println();

		//채널 3개 만듬
		System.out.println("=== [검증 테스트] 탈퇴한 유저(kim) 채널 초대 차단 ===");
		try {
			//삭제된 kim 넣고 채널 만들어보기 (예외 터져야함)
			Channel study = channelService.createChannel("공부하는 방", kim, han, park);
		} catch (RuntimeException e) {
			// 예외처리로 생성 x
			System.out.println("성공: 회원 검증 차단 성공! -> 에러 메시지: " + e.getMessage());
		}

		Channel study = channelService.createChannel("공부하는 방", lee, park, han, gong, nam);
		Channel game = channelService.createChannel("게임하는 방", lee, gong);
		Channel movie = channelService.createChannel("영화보는 방", nam, park);

		//채널 정보 조회
		System.out.println("채널 " + channelService.readChannel(study.getId()));
		System.out.println("채널 " + channelService.readChannel(game.getId()));
		System.out.println("채널 " + channelService.readChannel(movie.getId()));
		System.out.println();

		//채널 목록 조회
		List<Channel> channels = channelService.readAllChannel();
		System.out.println(" === 채널 목록 === ");
		for (Channel each : channels) {
			System.out.println(each.getChannelName());
		}
		System.out.println();

		//채널 정보 변경
		System.out.print("수정전: ");
		System.out.println(study);

		channelService.updateChannel(study.getId(), "공부 빡시게 하는 방", lee, gong, nam, han, park);
		System.out.print("수정후: ");
		System.out.println(study);
		System.out.println();

		channelService.deleteChannel(movie.getId());
		System.out.println("movie채널 삭제완료 남은 채널목록: ");
		for (Channel each : channels) {
			System.out.println(each.getChannelName());
		}
		System.out.println("채널 테스트 종료");
		System.out.println();


		try {
			System.out.println("=== 채널에 존재하지 않는 유저 차단 ===");
			//study 채널에 없는 친구가 메세지 보냄 (예외 터져야함)
			Message firstMessage = messageService.createMessage("반갑다", study, kim);
		} catch (RuntimeException e) {
			System.out.println("회원 차단 했습니다 에러메세지: " + e.getMessage());
		}

		//메세지 생성
		System.out.println();
		Message firstMessage = messageService.createMessage("반갑다", study, lee);
		Message secondMessage = messageService.createMessage("나도", study, park);
		Message thirdMessage = messageService.createMessage("난 안반갑다", study, han);

		//메세지 단건 조회
		System.out.println("=== 메세지 단건 조회 ===");
		System.out.print(messageService.readMessage(firstMessage.getId()).getValues());
		System.out.println("  -" + messageService.readMessage(firstMessage.getId()).getSender().getName());
		System.out.println();

		//메세지 내역 조회
		List<Message> messages = messageService.readAllMessage();
		System.out.println(" === 메세지 내역 === ");
		for (Message each : messages) {
			System.out.println(each.getValues() + "  -"  + each.getSender().getName());
		}
		System.out.println();

		//메세지 수정
		messageService.updateMessage(firstMessage.getId(), "매우 방갑다");
		System.out.println(" === 수정 메세지 내역 === ");
		for (Message each : messages) {
			System.out.println(each.getValues() + "  -"  + each.getSender().getName());
		}
		System.out.println();

		//메세지 삭제
		messageService.deleteMessage(secondMessage.getId());
		System.out.println(" === 삭제후 메세지 내역 === ");
		for (Message each : messages) {
			System.out.println(each.getValues() + "  -"  + each.getSender().getName());
		}
	}
}
