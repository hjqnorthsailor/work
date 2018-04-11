package com.tmooc.work.dao;

import com.tmooc.work.WorkApplicationTests;
import com.tmooc.work.entity.ReachTable;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class HuoYueDaoTest extends WorkApplicationTests {
    @PersistenceContext
    private EntityManager entityManager;
    @Test
    public void queryReachRate() {
        String sql="SELECT s.`qun_name` AS qunName,COUNT(s.`studentqq`) AS countNum,AVG(h.`percent`) AS rate" +
                " FROM student s JOIN huo_yue h" +
                " ON s.`studentqq`=h.`qq`" +
                " WHERE h.`month`=4 AND h.`week`=2" +
                " GROUP BY s.`qun_name`";
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("qunName", StandardBasicTypes.STRING)
                .addScalar("countNum", StandardBasicTypes.INTEGER)
                .addScalar("rate", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ReachTable.class));
        final List<ReachTable> resultList = query.getResultList();
        resultList.forEach(r-> System.out.println(r.getRate()));
    }
}