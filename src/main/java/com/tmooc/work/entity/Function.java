package com.tmooc.work.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Function extends BaseEntity{
    private String permission;//权限字符串


    @ManyToMany
    @JoinTable(name = "role_function", joinColumns = { @JoinColumn(name = "function_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private List<Role> roleList;

}
