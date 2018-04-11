package com.tmooc.work.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReachTable extends BaseEntity {
    private String qunName;
    private String countNum;
    private String rate;
}
