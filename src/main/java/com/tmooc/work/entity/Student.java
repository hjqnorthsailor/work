package com.tmooc.work.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.hql.internal.classic.ClauseParser;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author northsailor
 */
@Entity
@Data
public class Student extends BaseEntity{
    /**学生姓名*/
    private String studentName;
    /**学生qq*/
    private String studentQQ;
    /** 跟进阶段 0：未跟进，1：首次跟进，2-x 。。**/
    private Integer stage;
    /**学生所在群*/
    private String qunName;
    private String qunNum;
    /**学生所属方向*/
    private String teamName;
    /** 标签**/
    private Integer mark;
    /** 备注**/
    private String remark;
    @OneToMany(mappedBy = "student")
    @NotFound(action = NotFoundAction.IGNORE)
    @OrderBy("month desc,week desc")
    /** 达到率**/
    private List<ReachRate> reachRates;

}
