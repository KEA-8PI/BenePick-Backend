package com._pi.benepick.domain.draws.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import com._pi.benepick.domain.raffles.entity.Raffles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE draws SET is_deleted = 'T' WHERE id = ?")
public class Draws extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //추첨_id

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Raffles.class)
    @JoinColumn(name = "raffle_id")
    private Raffles raffleId; //응모_id

    private int sequence; // 순서

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // WINNER, WAITLIST, CANCLE, NOSHOW

    public void updateDeleted() {this.isDeleted = 'T';}
}
