package com._pi.benepick.domain.members.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String id; //사원_id
    private String name; //사원 이름
    private String deptName; //소속부서
    private String profileImg; //프로필사진
    private String password; //비밀번호
    private Long penaltyCnt; //잔여 패널티
    private Long point; //복지포인트
    @Enumerated(EnumType.ORDINAL)
    private Role role; //역할
}
