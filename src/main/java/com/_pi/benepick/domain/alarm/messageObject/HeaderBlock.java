package com._pi.benepick.domain.alarm.messageObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeaderBlock {
    private String type;
    private String text;
    private String style;
}
