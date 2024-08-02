package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsComposeService {
    GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file); //상품 파일 업로드
}
