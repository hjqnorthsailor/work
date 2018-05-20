package com.tmooc.work.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmooc.work.dao.GroupReachDao;
import com.tmooc.work.entity.GroupReach;
import com.tmooc.work.excel.ReachExcel;
import com.tmooc.work.service.ReachService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author northsailor
 */
@Service
public class ReachServiceImpl implements ReachService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private GroupReachDao groupReachDao;
    @Override
    public List<ReachExcel> queryReachRate(Integer month, Integer week) {
        String sql="SELECT s.`qun_name` AS qunName,COUNT(s.`studentqq`) AS countNum,AVG(h.`percent`) AS rate" +
                " FROM student s JOIN reach_rate h" +
                " ON s.`studentqq`=h.`qq`" +
                " WHERE h.`month`="+month+" AND h.`week`="+week+
                " GROUP BY s.`qun_name`";
        Query query = entityManager.createNativeQuery(sql);
        List<ReachExcel> resultList = query.unwrap(SQLQuery.class)
                    // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                    .addScalar("qunName", StandardBasicTypes.STRING)
                    .addScalar("countNum", StandardBasicTypes.INTEGER)
                    .addScalar("rate", StandardBasicTypes.DOUBLE)
                    .setResultTransformer(Transformers.aliasToBean(ReachExcel.class)).list();
        return resultList;
    }


    @Override
    public List<Map<String, Object>> findAllGroupReaches() {
        List<GroupReach> groupReaches = groupReachDao.findAll();
        if (groupReaches.size()>0) {
            List<Map<String, Object>> resultList = Lists.newArrayList();
            groupReaches.forEach(g -> {
                Map<String, Object> reachMap = Maps.newHashMap();
                reachMap.put("qunName", g.getQunName());
                reachMap.put("date", g.getMonth() + "." + g.getWeek());
                reachMap.put(g.getQunName()+g.getMonth() + "." + g.getWeek(), g.getRate());
                resultList.add(reachMap);
            });
            return resultList;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGroupReach(List<ReachExcel> list, Integer month, Integer week){
        List<GroupReach> groupReachList=Lists.newArrayList();
        list.forEach(l->{
            GroupReach groupReach=GroupReach.builder().qunName(l.getQunName())
                    .countNum(l.getCountNum()).rate(l.getRate()).month(month).week(week).build();
            groupReachList.add(groupReach);
        });
        groupReachDao.save(groupReachList);
    }
}
