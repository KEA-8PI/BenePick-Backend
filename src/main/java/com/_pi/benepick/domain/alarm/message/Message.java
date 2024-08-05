package com._pi.benepick.domain.alarm.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    @JsonProperty("email")
    private String email;

    @JsonProperty("text")
    private String text;

    @JsonProperty("blocks")
    private List<Object> blocks;

    public Message(String email, String text) {
        this.email = email;
        this.text = text;
    }
}
