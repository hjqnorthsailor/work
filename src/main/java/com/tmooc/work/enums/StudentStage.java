package com.tmooc.work.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
public enum StudentStage {
    NOFOLLOWUP(0,"未跟进"),
    FOLLOWUP(1,"已跟进");
    @Getter
    @Setter
    private Integer stage;
    @Getter
    @Setter
    private String msg;
}
