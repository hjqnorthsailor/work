package com.tmooc.work.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class_name: Profession
 * package: com.tmooc.work.entity
 * describe: TNOOC 方向
 * creat_user: wanwt@senthinkcom
 * creat_date: 2018/3/16
 * creat_time: 下午10:41
 **/
@Entity
@Getter
@Setter
public class Profession extends BaseEntity{
    private String professionName;
    private Integer score;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "profession")
    private List<Teacher> teacherList=new ArrayList<>();
}
