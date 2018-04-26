package com.tmooc.work.dao;

import com.tmooc.work.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDao extends JpaRepository<SysPermission,Integer> {
}
