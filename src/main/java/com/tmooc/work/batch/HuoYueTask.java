package com.tmooc.work.batch;

import com.tmooc.work.dao.HuoYueDao;
import com.tmooc.work.entity.HuoYue;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveAction;
@Slf4j
/**
 * 使用forkjoin导入活跃度数据
 */
public class HuoYueTask extends RecursiveAction {
    private static final int THRESHOLD=101;
    private int start;
    private int end;
    private List<HuoYue> list;
    private HuoYueDao huoYueDao;
    public HuoYueTask(List<HuoYue> list,HuoYueDao huoYueDao){
        this.start=0;
        this.end=list.size();
        this.list=list;
        this.huoYueDao=huoYueDao;
    }
    @Override
    protected void compute() {
        boolean canComputer=(end-start)<=THRESHOLD;
        if (canComputer){
            huoYueDao.save(list);
            log.info("插入了"+list.size()+"条数据");
        }else {
            int middle=(start+end)/2;
            List<HuoYue> leftList = list.subList(start, middle);//list的subList方法默认截取第一个参数到第二个参数的值，但不包含第二个参数
            List<HuoYue> rightList = list.subList(middle, end);
            HuoYueTask left=new HuoYueTask(leftList,huoYueDao);
            HuoYueTask right=new HuoYueTask(rightList,huoYueDao);
            invokeAll(left,right);
        }

    }

    public static void main(String[] args) {
        List<Integer> list=new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        List<Integer> list1 = list.subList(1, 4);
        System.out.println(Arrays.toString(list1.toArray()));
    }
}
