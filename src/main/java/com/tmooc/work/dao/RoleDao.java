package com.tmooc.work.dao;

import com.tmooc.work.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<SysRole,Integer> {
}
