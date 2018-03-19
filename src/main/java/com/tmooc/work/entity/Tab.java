package com.tmooc.work.entity;
import lombok.Getter;
import lombok.Setter;

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
    private String tagName;
    @ManyToOne
    private Tab parent;
    @OneToMany
    private List<Tab> children;

}
