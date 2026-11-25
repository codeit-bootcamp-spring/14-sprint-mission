package com.sprint.mission.discodeit.domain.service.binarycontent;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.util.file.FileUpload;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final FileUpload fileUpload;

    @Override
    public List<BinaryContent> storeFiles(List<MultipartFile> multipartFiles) {

        List<BinaryContent> storeFileResult = fileUpload.uploadFiles(multipartFiles);

        if(!storeFileResult.isEmpty()){
            for (BinaryContent binaryContent : storeFileResult) {
                binaryContentRepository.save(binaryContent);
            }
        }

        return storeFileResult;
    }

    @Override
    public BinaryContent storeFile(MultipartFile multipartFile) {
        BinaryContent uploadedFile = fileUpload.uploadFile(multipartFile);

        return binaryContentRepository.save(uploadedFile);
    }

    @Override
    public BinaryContent findStoreFile(UUID binaryContentUUID) {

        //안에 실제 저장소 주소 들어있음
        return binaryContentRepository.findById(binaryContentUUID)
            .orElseThrow(() -> new CustomException(CustomErrorCode.FILE_NOT_FOUND));
    }

    @Override
    public List<BinaryContent> findAllStoreFileByIdIn(List<UUID> fileIdList) {

        List<BinaryContent> binaryContentList = new ArrayList<>();
        fileIdList.stream()
            .forEach(id -> binaryContentList.add(this.findStoreFile(id)));

        return binaryContentList;
    }

    /*
        todo : 멤버 / 메시지 해당 url 필드 값 변경하기
     */
    @Override
    public void deleteStoreFileById(UUID binaryContentUUID) {

        BinaryContent storeFile = this.findStoreFile(binaryContentUUID);
        String filePath = storeFile.getPathUrl();

        File file = new File(filePath);
        boolean isDeleted = file.delete();

        log.info("파일 삭제 시작 - 경로 : {}", filePath);
        if(!isDeleted){
            throw new CustomException(CustomErrorCode.FILE_DELETE_FAILED);
        }

        binaryContentRepository.delete(binaryContentUUID);
    }

    @Override
    public List<BinaryContent> findAllStoreFile() {
        return binaryContentRepository.findAll();
    }

}