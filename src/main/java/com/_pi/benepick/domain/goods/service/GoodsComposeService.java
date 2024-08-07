package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.members.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsComposeService {
    GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file); //상품 파일 업로드
    GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> goodsDeleteRequestDTO, Members members); //상품 삭제
}
