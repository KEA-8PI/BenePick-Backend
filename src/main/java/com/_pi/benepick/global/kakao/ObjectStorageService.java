package com._pi.benepick.global.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 일정시간마다 카카오에서 Token 발급

/*
    삭제, 조회 등의 서비스 확인 필요
 */

@Service
public class ObjectStorageService {
    @Autowired
    private S3Client s3Client;
    @Value("${cloud.kakao.object-storage.bucket}")
    private String bucketName;
    @Value("${cloud.kakao.object-storage.project-id}")
    private String projectID;
    @Value("${cloud.kakao.object-storage.endpoint}")
    private String endpoint;

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // objectKey : 업로드할 파일명
// filePath : 업로드할 파일이 위치한 로컬 경로
    public List<String> uploadFile(List<MultipartFile> multipartFiles)  {
        List<String> fileList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileList에 추가
        multipartFiles.forEach(file -> {
            try {
                Path path = Files.createTempFile(null,null);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                String fileName = createFileName(file.getOriginalFilename());

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

                s3Client.putObject(putObjectRequest, path);

                fileList.add(endpoint + "/v1/" + projectID + "/" + bucketName + "/" + fileName);

                Files.delete(path);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "파일 업로드에 실패했습니다.");
            }

        });
        return fileList;
    }

    // 엑셀 파일에서 추출한 사진 업로드
    public List<String> uploadExcelFile(List<File> files) {
        List<String> fileList = new ArrayList<>();

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Path path = Files.createTempFile(null, null);
                Files.copy(fis, path, StandardCopyOption.REPLACE_EXISTING);

                String fileName = createFileName(file.getName());

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                s3Client.putObject(putObjectRequest, path);

                fileList.add(endpoint + "/v1/" + projectID + "/" + bucketName + "/" + fileName);

                Files.delete(path);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "파일 업로드에 실패했습니다.");
            }
        }
        return fileList;
    }
}
