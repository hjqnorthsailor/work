package com.tmooc.work.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Student extends BaseEntity{
    private String studentName;
    @Excel(name="qq",orderNum = "0")
    private String studentQQ;
    /** 跟进阶段 0：未跟进，1：首次跟进，2-x 。。**/
    private Integer stage;
    @Excel(name="群名称",orderNum = "1")
    private String qunName;
    @Excel(name="群号",orderNum = "2")
    private String qunNum;
    /** 备注**/
    private String remark;
}
