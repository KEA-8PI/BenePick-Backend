package com._pi.benepick.domain.members.entity;

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


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Members extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; //멤버_id
    private String messengerId; //메신저아이디
    private String memberName; //사원 이름
    private String deptName; //소속부서
    private String profileImg; //프로필사진
    private String pwd; //비밀번호
    private Long remainPenalty; //잔여 패널티
    private Long point; //복지포인트
    private Role role; //역할
    private Status status; //상태

}
