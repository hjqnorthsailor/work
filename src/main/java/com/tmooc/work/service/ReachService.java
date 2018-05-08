package com.tmooc.work.service;

import com.tmooc.work.excel.Reach;

import java.util.List;

public interface ReachService {
    List<Reach>  queryReachRate(Integer month, Integer week);
}
