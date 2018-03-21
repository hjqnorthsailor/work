package com.tmooc.work.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
@Setter
@Getter
public class PageDTO implements Serializable {
    private Integer pageNo=1;
    private Integer size=10;
    private String direction= Sort.Direction.DESC.name();
    private String properties="";
}
