package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id")
    private Raffles raffle; //응모_id

    private int sequence; // 순서

    @Enumerated(EnumType.STRING)
    private Status status; // WINNING, WAITLIST, CANCEL, NO_SHOW
}