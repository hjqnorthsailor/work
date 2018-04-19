package com.tmooc.work.dao;

import com.tmooc.work.WorkApplicationTests;
import com.tmooc.work.entity.Reach;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class HuoYueDaoTest extends WorkApplicationTests {
    @PersistenceContext
    private EntityManager entityManager;
    @Test
    public void queryReachRate() {
        Integer month=4;
        Integer week=2;
        String sql="SELECT s.`qun_name` AS qunName,COUNT(s.`studentqq`) AS countNum,AVG(h.`percent`) AS rate" +
                " FROM student s JOIN huo_yue h" +
                " ON s.`studentqq`=h.`qq`" +
                " WHERE h.`month`="+month+" AND h.`week`="+week+"" +
                " GROUP BY s.`qun_name`";
        Query query = entityManager.createNativeQuery(sql);
        final List<Reach> resultList = query.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("qunName", StandardBasicTypes.STRING)
                .addScalar("countNum", StandardBasicTypes.INTEGER)
                .addScalar("rate", StandardBasicTypes.DOUBLE)
                .setResultTransformer(Transformers.aliasToBean(Reach.class)).list();
        resultList.forEach(r-> System.out.println(r.getRate()));
    }
}