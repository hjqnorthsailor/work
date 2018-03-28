package com.tmooc.work.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
public class Team extends BaseEntity {
    /** 小组名 **/
    private String team_name;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "team")
    private List<Teacher> teachers;



}
