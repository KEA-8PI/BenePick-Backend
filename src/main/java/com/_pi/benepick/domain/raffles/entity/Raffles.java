package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import com._pi.benepick.domain.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Raffles extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //응모_id
    private String memberId; //멤버_id
    private Long goodsId; //상품_id
    private Long point; //사용포인트
    private Result raffleResult; //추첨 결과
    private Long waitlistNum; //대기번호
    private LocalDateTime raffleStartAt; //응모 시작일
    private LocalDateTime raffleEndAt; //응모 종료일
    private Status status; //상태

}
