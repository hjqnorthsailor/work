package com.tmooc.work.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * @author northsailor
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReachRate extends BaseEntity{
    @Excel(name="方向",orderNum = "0")
    private String major;
    @Excel(name="QQ",orderNum = "1")
    private String qq;
    @Excel(name="达到率",orderNum = "2")
    private Double percent;
    @Excel(name="最后登录时间",orderNum = "3")
    private Date lastLoginTime;
    @Excel(name="月份",orderNum = "4")
    private String month;
    @Excel(name="周",orderNum = "5")
    private String week;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Student student;
}
