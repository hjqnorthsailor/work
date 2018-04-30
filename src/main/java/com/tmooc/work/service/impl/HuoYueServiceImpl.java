package com.tmooc.work.service.impl;

import com.tmooc.work.dao.HuoYueDao;
import com.tmooc.work.entity.HuoYue;
import com.tmooc.work.entity.Student;
import com.tmooc.work.service.HuoYueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class HuoYueServiceImpl implements HuoYueService {
    @Autowired
    private HuoYueDao huoYueDao;
    @Override
    @Transactional
    public void save(List<HuoYue> list) {
        huoYueDao.save(list);
    }
}
