package com._pi.benepick.controller;
import com._pi.benepick.global.kakao.ObjectStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지 API")
public class ImageController {
    private final ObjectStorageService s3Service;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드", description = "이미지를 오브젝트 스토리지에 올리고 url을 반환합니다.")
    public String uploadObject(@RequestPart MultipartFile file) {
        List<MultipartFile> list = new ArrayList<>();
        list.add(file);
        return s3Service.uploadFile(list).get(0);
    }
}
