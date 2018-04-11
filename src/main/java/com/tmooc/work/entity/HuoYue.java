package com.tmooc.work.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HuoYue extends BaseEntity{
    @Excel(name="qq",orderNum = "0")
    private String qq;
    @Excel(name="达到率",orderNum = "1")
    private Double percent;
    @Excel(name="月份",orderNum = "2")
    private String month;
    @Excel(name="周",orderNum = "3")
    private String week;
}
