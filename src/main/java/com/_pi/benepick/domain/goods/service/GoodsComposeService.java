package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface GoodsComposeService {
    GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file, Members member); //상품 파일 업로드
    GoodsResponse.GoodsAddResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO, Members member); //상품 추가
    GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO, Members member); //상품 수정
    GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> goodsDeleteRequestDTO, Members member); //상품 삭제
    GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId, Members member); // 상품 상세 조회
    GoodsResponse.GoodsListSearchResponseDTO searchGoods(GoodsStatus goodsStatus, Integer page, Integer size, String keyword, GoodsFilter sortBy, String category, Members member); //상품 검색
    List<Goods> getGoodsList(String categoryName, LocalDate startDate, LocalDate endDate);
}
