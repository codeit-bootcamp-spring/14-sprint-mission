package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BinaryContent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    String fileName;
    long size;
    String contentType;
    byte[] bytes;

    private BinaryContent(String fileName, long size, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fileName;
        private long size;
        private String contentType;
        private byte[] bytes;

        public Builder fileName(String fileName){
            this.fileName = fileName;
            return this;
        }
        public Builder size(long size){
            this.size = size;
            return this;
        }
        public Builder contentType(String contentType){
            this.contentType = contentType;
            return this;
        }
        public Builder bytes(byte[] bytes){
            this.bytes = bytes;
            return this;
        }

            public BinaryContent build(){
            return new BinaryContent(this.fileName,this.size,this.contentType,this.bytes);
            }


    }


}
