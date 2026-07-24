package com.sprint.mission.discodeit.file.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.file.repository.ChannelRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class FilechannelRepository implements ChannelRepository {
    private final static List<Channel> channels = new ArrayList<>();

    @Override
    public void channelLoad() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data/Channel.ser"))) {
            channels.addAll((List<Channel>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("기존 채널 데이터가 없습니다.");
        }
    }

    @Override
    public void channelFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/Channel.ser"))) {
            objectOutputStream.writeObject(this.channels);
        } catch (IOException e) {
            System.out.println("채널을 저장했습니다.");
        }
    }

    @Override
    public List<String> readChannelName() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/Channel.txt"))) {
            String name;
            List<String> channelName = new ArrayList<>();
            while ((name = bufferedReader.readLine()) != null) {
                channelName.add(name);
            }
            return channelName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Channel> channelAdd(List<Channel> channels) {
        this.channels.addAll(channels);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/Channel.ser"))) {
            objectOutputStream.writeObject(this.channels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.channels;
    }

    @Override
    public Optional<Channel> findByChannel(String channelName) {
        return channels.stream()
                .filter(channel -> channelName.equals(channel.getChannelName()))
                .findFirst();
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel);
    }

    @Override
    public List<Channel> findAllChannel() {
        return new ArrayList<>(channels);
    }
}
