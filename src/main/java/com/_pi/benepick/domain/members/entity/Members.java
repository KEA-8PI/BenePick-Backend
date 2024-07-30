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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE members SET is_deleted = 'T' WHERE id = ?")
public class Members extends BaseJPATimeEntity {
    @Id
    private String id; //사원_id
    @Column(nullable = false)
    private String name; //사원 이름
    @Column(nullable = false)
    private String deptName; //소속부서
    @Column(nullable = false)
    private String password; //비밀번호
    @Column(nullable = false)
    private Long penaltyCnt; //잔여 패널티
    @Column(nullable = false)
    private Long point; //복지포인트
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //역할

    private String profileImg; //프로필사진

    public static Members createMember(String id,String deptName,String name,Long point,Long penaltyCnt,Role role,String password){
        return new Members(id,name,deptName,password,penaltyCnt,point,role,password);
    }
}
