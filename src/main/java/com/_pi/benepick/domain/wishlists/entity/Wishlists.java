package com._pi.benepick.domain.wishlists.entity;

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

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlists extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //위시리스트_id
    private String memberId; //멤버_id
    private Long goodsId; //상품_id
}
