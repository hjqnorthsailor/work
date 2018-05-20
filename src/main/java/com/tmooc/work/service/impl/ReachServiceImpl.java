package com.tmooc.work.service.impl;

import com.tmooc.work.excel.Reach;
import com.tmooc.work.service.ReachService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
/**
 * @author northsailor
 */
@Service
public class ReachServiceImpl implements ReachService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Reach> queryReachRate(Integer month, Integer week) {
        String sql="SELECT s.`qun_name` AS qunName,COUNT(s.`studentqq`) AS countNum,AVG(h.`percent`) AS rate" +
                " FROM student s JOIN reach_rate h" +
                " ON s.`studentqq`=h.`qq`" +
                " WHERE h.`month`="+month+" AND h.`week`="+week+
                " GROUP BY s.`qun_name`";
        Query query = entityManager.createNativeQuery(sql);
        final List<Reach> resultList = query.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("qunName", StandardBasicTypes.STRING)
                .addScalar("countNum", StandardBasicTypes.INTEGER)
                .addScalar("rate", StandardBasicTypes.DOUBLE)
                .setResultTransformer(Transformers.aliasToBean(Reach.class)).list();
        return resultList;
    }
}
