package com._pi.benepick.domain.members.entity;

import com._pi.benepick.domain.members.dto.MembersRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

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

    public void updateInfo(MembersRequest.MembersRequestDTO membersRequestDTO){
            this.point = Objects.nonNull( membersRequestDTO.getPoint())? membersRequestDTO.getPoint() : this.point;
            this.name = Objects.nonNull( membersRequestDTO.getName())? membersRequestDTO.getName() : this.name;
            this.deptName = Objects.nonNull( membersRequestDTO.getDeptName())? membersRequestDTO.getDeptName() : this.deptName;
            this.penaltyCnt = Objects.nonNull( membersRequestDTO.getPenaltyCnt())? membersRequestDTO.getPenaltyCnt() : this.penaltyCnt;
            this.role = Objects.nonNull( membersRequestDTO.getRole())? membersRequestDTO.getRole() : this.role;
    }

    public void decreasePoint(Long point) {
        this.point = this.point - point;
    }

    public void increasePoint(Long point) {
        this.point = this.point + point;
    }

    public void updatePenalty(Long penaltyCnt) {
        this.penaltyCnt = penaltyCnt;
    }
    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void initPassword( PasswordEncoder passwordEncoder){
        // detault 비밀번호 0000 hash 값
        this.password=passwordEncoder.encode("9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0");
    }

}
