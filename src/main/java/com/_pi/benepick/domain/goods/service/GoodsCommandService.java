package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsCommandService {
    //상품 엑셀 파일로 추가
    GoodsResponse.GoodsDetailResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO); //상품 추가
    GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file); //상품 파일 업로드
    GoodsResponse.GoodsDetailResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO); //상품 수정
    GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> goodsDeleteRequestDTO); //상품 삭제
}
