package com.tmooc.work.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudentEntity extends BaseEntity {
    @Excel(name="成员",orderNum = "0")
    private String member;
    @Excel(name="名片",orderNum = "1")
    private String card;
    @Excel(name="QQ",orderNum = "2")
    private String qq;
    @Excel(name="群名称",orderNum = "3")
    private String qunName;
    @Excel(name="群号",orderNum = "4")
    private String qunQQ;
}
