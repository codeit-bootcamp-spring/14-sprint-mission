package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.repository.serverRepository;

import java.util.ArrayList;
import java.util.List;


public abstract class JCFserverRepository implements serverRepository {
    protected final List<Server> servers = new ArrayList<>();

    @Override
    public void serverCreate(String serverName) {
        boolean serverExists = true;
        for (Server each:servers){
            if (each.getServerName().equals(serverName)){
                System.out.println("이미 생성된 서버의 이름입니다: "+serverName);
                serverExists = false;
            }
        }
        if (serverExists){
            System.out.println("서버 생성이 완료되었습니다: "+serverName);
            servers.add(new Server(serverName));
        }
    }

    @Override
    public Server findByServer(String serverName) {
        for (Server server:servers){
            if (server.getServerName().equals(serverName)){
                return server;
            }
        }
        System.out.println("찾는 서버의 이름이 없습니다: "+serverName);
        return null;
    }

    @Override
    public void serverUpdate(String serverName, String updateServerName) {
        Server server = findByServer(serverName);
        if (server != null){
            System.out.println(serverName+" 서버의 이름을 "+updateServerName+"로 수정했습니다.");
            server.updateName(updateServerName);
        }
    }

    @Override
    public void serverDelete(String serverName) {
        Server server = findByServer(serverName);
        if (server != null){
            System.out.println(serverName+" 서버를 삭제했습니다.");
            servers.remove(server);
        }
    }
}
