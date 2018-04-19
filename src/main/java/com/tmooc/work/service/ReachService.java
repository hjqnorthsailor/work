package com.tmooc.work.service;

import com.tmooc.work.entity.Reach;

import java.util.List;

public interface ReachService {
    List<Reach>  queryReachRate(Integer month, Integer week);
}
