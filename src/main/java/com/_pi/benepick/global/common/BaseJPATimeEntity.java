package com._pi.benepick.global.common;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseJPATimeEntity implements Serializable {

    // Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 조회한 Entity 값을 변경할 때 시간이 자동 저장됩니다.
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 삭제 여부 ( 'F', 'T' )
    @Column(nullable = false)
    protected char isDeleted = 'F';

    protected void update(){
        updatedAt = LocalDateTime.now();
    }
}
