package com.tmooc.work.service.impl;

import com.tmooc.work.dao.TabDao;
import com.tmooc.work.entity.Tab;
import com.tmooc.work.service.TabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TabServiceImpl implements TabService {
    @Autowired
    private TabDao tabDao;
    @Override
    public List<Tab> findAll() {
        return tabDao.findAll();
    }
}
