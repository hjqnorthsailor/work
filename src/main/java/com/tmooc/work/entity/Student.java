package com.tmooc.work.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.tmooc.work.enums.StudentMark;
import com.tmooc.work.enums.StudentStage;
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
    private Integer stage=StudentStage.NOFOLLOWUP.getStage();
    private String qunName;
    private String qunNum;
    /** 标签**/
    private Integer mark=StudentMark.NOMARK.getMark();

}
