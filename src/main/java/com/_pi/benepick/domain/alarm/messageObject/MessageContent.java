package com._pi.benepick.domain.alarm.messageObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MessageContent {

    @JsonProperty("email")
    private String email;

    @JsonProperty("text")
    private String text;

    @JsonProperty("blocks")
    private List<Object> blocks;

    public MessageContent(String email, String text) {
        this.email = email;
        this.text = text;
    }
}
