package com.tmooc.work.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum StudentMark {
    NOMARK(0,"未标记"),
    NORMAL(1,"正常"),
    UNRELATED(2,"未联系到"),
    UNRELATEDANDUNLOGIN(3,"为联系到并且长时间未登录"),
    EXPIRE(4,"账户过期"),
    OTHER(5,"其他(试听、休学、查询不到)");
    @Getter
    @Setter
    private Integer mark;
    @Getter
    @Setter
    private String msg;

}
