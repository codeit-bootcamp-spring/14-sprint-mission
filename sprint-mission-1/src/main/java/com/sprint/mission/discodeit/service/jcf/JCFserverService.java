package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.serverRepository;
import com.sprint.mission.discodeit.service.serverService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JCFserverService extends JCFserverRepository implements serverRepository {
    Scanner sc = new Scanner(System.in);

    @Override
    public void allPrintServer() {
        for (Server server:super.servers){
            System.out.printf("서버의 이름: %s 입니다.\n", server.getServerName());
        }
    }

    @Override
    public void printServer() {
        System.out.print("자세히 보고자 하는 서버의 이름을 말해주세요: ");
        String serverName = sc.next();
        Server server = findByServer(serverName);

        System.out.println(server.toString());
    }

    @Override
    public Server selectedServer() {
        allPrintServer();
        System.out.print("선택할 서버의 이름을 말해주세요: ");
        String serverName = sc.next();
        Server server = findByServer(serverName);

        if (server != null){
            return server;
        }

        System.out.println("서버를 찾을 수 없습니다.");
        return null;
    }
}
