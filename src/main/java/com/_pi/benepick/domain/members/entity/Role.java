package com._pi.benepick.domain.members.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN,
    MEMBER;

    @JsonCreator
    public static Role fromRoleStatus(String val) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(val))
                .findAny()
                .orElse(null);
    }
}
