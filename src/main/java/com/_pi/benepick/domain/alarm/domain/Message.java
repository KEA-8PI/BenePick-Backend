package com._pi.benepick.domain.alarm.domain;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE message SET is_deleted = 'T' WHERE id = ?")
public class Message extends BaseJPATimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String contents;

    public static Message of(String email, String name, String contents) {
        return Message.builder()
                .email(email)
                .name(name)
                .contents(contents)
                .build();
    }

}
