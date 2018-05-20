package com.tmooc.work.entity;

import lombok.*;

import javax.persistence.Entity;

/**
 * @author northsailor
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note extends BaseEntity{
    private Integer month=1;
    private Integer week=1;
    private Integer weekDay=1;
    private Integer answer=0;
    private Integer remote=0;
    private Integer count=0;
    private Integer validCount=0;
    private Integer broadcast=0;
    private Integer inputTitle=0;
    private Integer forward=0;
    private Integer other=0;
}
