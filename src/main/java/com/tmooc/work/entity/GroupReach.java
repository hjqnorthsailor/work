package com.tmooc.work.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author northsailor
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupReach extends BaseEntity {
    private String qunName;
    private Integer countNum;
    private Double rate;
    private Integer month;
    private Integer week;
}
