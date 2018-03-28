package com.tmooc.work.entity;



import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
public class Role extends BaseEntity {
    private String roleName; //角色名称
    private String roleDescription;//角色描述
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "roleList")//立即从数据库中进行加载数据;
    @Fetch(FetchMode.SELECT)
    private List<Teacher> teacherList=new ArrayList<>();
    @ManyToMany
    @JoinTable(name="role_function",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={
            @JoinColumn(name="function_id")})
    private List<Function> functionList=new ArrayList<>();
}
