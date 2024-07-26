package com._pi.benepick.domain.winners.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import com._pi.benepick.domain.raffles.entity.Raffles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE winners SET is_deleted = 'T' WHERE id = ?")
public class Winners extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //추첨_id

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Raffles.class)
    @JoinColumn(name = "raffle_id")
    private Raffles raffleId; //응모_id

    private int sequence; // 순서

    @Enumerated(EnumType.STRING)
    private Status status; // WINNER, WAITLIST, CANCLE, NOSHOW

}
