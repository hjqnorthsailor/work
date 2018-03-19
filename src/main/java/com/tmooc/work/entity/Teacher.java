package com.tmooc.work.entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class_name: Teacher
 * package: com.tmooc.work.entity
 * describe: TMOOC 助教
 * creat_user: wanwt@senthinkcom
 * creat_date: 2018/3/16
 * creat_time: 下午10:44
 **/
@Entity
@Setter
@Getter
public class Teacher extends BaseEntity {

    private String teacherName;
    //@Column(name = "email",columnDefinition = "账号，邮箱")
    private String email;
    //@Column(name = "phone",columnDefinition = "联系电话")
    private String phone;
    //@Column(name = "nike_name",columnDefinition = "教师昵称")
    private String nikeName;
    //@Column(name = "login_pwd",columnDefinition = "登陆密码")
    private String loginPwd;
    //@Column(name = "salt",columnDefinition = "加密盐")
    private String salt;
    //@Column(name = "position",columnDefinition = "职位")
    private String position;
    private String imgUrl;
    @ManyToOne
    //@Column(columnDefinition = "所属方向")
    private Profession profession;
    @ManyToOne
    //@Column(columnDefinition = "所属小组")
    private Team team;
    @ManyToMany(fetch = FetchType.EAGER)//立即从数据库中进行加载数据;
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "teacher_role",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")})
    private List<Role> roleList=new ArrayList<>();
    //@Column(name = "entry_time",columnDefinition = "入职时间")
    private Date entryTime;
    @UpdateTimestamp
    //@Column(name = "last_login_time",columnDefinition = "上次登录时间")
    private Date lastLoginTime;
    //@Column(name = "status",columnDefinition = "账号状态")
    private Integer status;

}
