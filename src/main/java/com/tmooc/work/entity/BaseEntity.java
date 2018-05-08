package com.tmooc.work.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bin
 *
 */
@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID自动生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    /**
     * 创建时间
     */
    @Column(name = "create_date_time")
    @CreatedDate
    @JsonIgnore
    protected Date createDateTime;

    /**
     * 最后修改时间
     */
    @Column(name = "update_date_time")
    @LastModifiedDate
    @JsonIgnore
    protected Date updateDateTime;
    /**
     * 操作人
     */
    @LastModifiedBy
    protected  String user;
;
}