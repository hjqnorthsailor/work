package com.tmooc.work.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.tmooc.work.entity.BaseEntity;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reach extends BaseEntity {
    @Excel(name="群名称",orderNum = "0")
    private String qunName;
    @Excel(name="有效人数",orderNum = "1")
    private Integer countNum;
    @Excel(name="达到率",orderNum = "2")
    private double rate;
}
