package com.tmooc.work.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class StudentExcel {
    @Excel(name="qq",orderNum = "0")
    private String studentQQ;
    /** 跟进阶段 0：未跟进，1：首次跟进，2-x 。。**/
    private Integer stage;
    @Excel(name="群名称",orderNum = "1")
    private String qunName;
    @Excel(name="群号",orderNum = "2")
    private String qunNum;
}
