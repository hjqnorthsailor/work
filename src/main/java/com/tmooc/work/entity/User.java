package com.tmooc.work.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author northsailor
 */
@Entity
@Data
public class User implements Serializable {
    /**
     * 主键ID自动生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    /**账号*/
    private String username;
    /**名称（昵称或者真实姓名，不同系统不同定义）*/
    private String name;
    /**密码*/
    private String password;
    @Email
    /**邮箱*/
    private String email;
    /**加密密码的盐*/
    private String salt;
    /**用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.*/
    private byte state;
    //立即从数据库中进行加载数据;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    /**一个用户具有多个角色*/
    private List<SysRole> roles;;
}
