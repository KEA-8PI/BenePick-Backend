package com._pi.benepick.domain.draws.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Status {
    WINNER,
    WAITLIST,
    CANCEL,
    NO_SHOW,
    NON_WINNER,
    CONFIRM;

    // Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static Status fromEventStatus(String val) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(val))
                .findAny()
                .orElse(null);
    }
}