package com.tmooc.work.entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parent;

import javax.persistence.*;
import java.util.*;
/**
 * 标签
 */
@Entity
@Setter
@Getter
public class Tab extends BaseEntity {
    private String href;
    private String dataUrl;
    private String tabName;
    private String icon;
    private Integer parentId;
    @ManyToOne
    @Transient
    private Parent parent;
    @OneToMany
    private List<Tab> children;

}
