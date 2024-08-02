package com._pi.benepick.domain.members.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@DynamicUpdate //변경사항이 있는 것만 Update
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE members SET is_deleted = 'T' WHERE id = ?")
public class Members extends BaseJPATimeEntity {
    @Id
    private String id; //사원_id

    @Column(nullable = false)
    private String name; //사원 이름

    @Column(nullable = false)
    private String deptName; //소속부서

    @Builder.Default
    @Column(nullable = false)
    private String password="0000"; //비밀번호

    @Column(nullable = false)
    private Long penaltyCnt; //잔여 패널티
    @Column(nullable = false)
    private Long point; //복지포인트
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //역할

    private String profileImg; //프로필사진

<<<<<<< HEAD
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setPenaltyCnt(Long penaltyCnt) {
        this.penaltyCnt = penaltyCnt;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
=======
>>>>>>> a433cb589ffda2a02997174fd8c1bf93c4f9dec6
}
