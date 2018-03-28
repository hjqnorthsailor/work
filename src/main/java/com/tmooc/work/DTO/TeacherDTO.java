package com.tmooc.work.DTO;

import com.tmooc.work.entity.Profession;
import com.tmooc.work.entity.Role;
import com.tmooc.work.entity.Team;
import lombok.*;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO implements Serializable{
        private Integer teacherId;
        private String teacherName;
        private String email;
        private String phone;
        private String nikeName;
        private String salt;
        private String position;
        private String imgUrl;
        private Profession profession;
        private Team team;
        private List<Role> roleList=new ArrayList<>();
        private Date createTime;
        private Date entryTime;
        private Date lastLoginTime;
        private Integer status;

    }
