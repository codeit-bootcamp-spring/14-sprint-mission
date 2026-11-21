package com.sprint.mission.discodeit.channel.domain.entity;

import com.sprint.mission.discodeit.baseentity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


@ToString
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

    @Column(name = "name")
    String channelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    ChannelType channelType;    //생성 시점에 만들어지는 필수값으로 변경 디폴트값 삭제

    @Column(name = "description")
    String description;


    static public Channel init(String channelName, ChannelType channelType, String description){
        return Channel.builder()
            .channelName(channelName)
            .channelType(channelType)
            .description(description)
            .build();
    }

    public void updateChannelNameAndDescription(String channelName, String description){
        validUpdatable();
        this.channelName = channelName;
        this.description = description;
    }

    public boolean isPrivate(){
        return this.channelType == ChannelType.PRIVATE;
    }

    private void validUpdatable(){
        if(isPrivate()){
            throw new CustomException(CustomErrorCode.CHANNEL_PRIVATE_CANT_UPDATE);
        }
    }
}
