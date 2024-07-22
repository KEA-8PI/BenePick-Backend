package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Winners extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //추첨_id
    private Long raffleId; //응모_id
    private int sequence; // 순서
    private Status status; // raffles/entity/Status WINNER, WAITLIST, NONWINNER

}
