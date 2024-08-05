package com._pi.benepick.domain.alarm.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageBlock {

    @JsonProperty("type")
    private String type;

    @JsonProperty("url")
    private String url;
}
