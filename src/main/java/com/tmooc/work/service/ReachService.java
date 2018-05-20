package com.tmooc.work.service;

import com.tmooc.work.excel.ReachExcel;

import java.util.List;
import java.util.Map;

public interface ReachService {
    List<ReachExcel>  queryReachRate(Integer month, Integer week);
    List<Map<String,Object>> findAllGroupReaches();
    void saveGroupReach(List<ReachExcel> list,Integer month,Integer week);
}
