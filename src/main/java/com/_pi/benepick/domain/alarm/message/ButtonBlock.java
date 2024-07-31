package com._pi.benepick.domain.alarm.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ButtonBlock {

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    @JsonProperty("style")
    private String style;

    @JsonProperty("action")
    private Action action;

    public ButtonBlock(String type, String text, String style) {
        this.type = type;
        this.text = text;
        this.style = style;
    }
}
