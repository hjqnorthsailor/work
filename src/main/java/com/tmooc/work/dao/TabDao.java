package com.tmooc.work.dao;

import com.tmooc.work.entity.Tab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface TabDao extends JpaRepository<Tab,Integer> {
}
