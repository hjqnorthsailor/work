package com.tmooc.work.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Student extends BaseEntity{
    private String studentName;
    private String studentQQ;
    /** 跟进阶段 0：未跟进，1：首次跟进，2-x 。。**/
    private Integer stage;
    private String qunName;
    private String qunNum;
    /** 备注**/
    private String remark;


}
