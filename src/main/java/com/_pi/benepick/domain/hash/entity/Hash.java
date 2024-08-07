package com._pi.benepick.domain.hash.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 PROTECTED로 수정
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE hash SET is_deleted = 'T' WHERE id = ?")
public class Hash extends BaseJPATimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String hash;

    @Column
    private Double seed;

    public void updateDeleted() {this.isDeleted = 'T';}
}