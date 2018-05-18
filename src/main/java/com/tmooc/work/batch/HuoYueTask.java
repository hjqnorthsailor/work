package com.tmooc.work.batch;

import com.tmooc.work.entity.ReachRate;
import com.tmooc.work.service.HuoYueService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.concurrent.RecursiveAction;
@Slf4j
/**
 * 使用forkjoin导入活跃度数据
 */
public class HuoYueTask extends RecursiveAction {
    private static final int THRESHOLD=100;
    private int start;
    private int end;
    private List<ReachRate> list;
    private HuoYueService huoYueService;
    public HuoYueTask(List<ReachRate> list, HuoYueService huoYueService){
        this.start=0;
        this.end=list.size();
        this.list=list;
        this.huoYueService=huoYueService;
    }
    @Override
    protected void compute() {
        boolean canComputer=(end-start)<=THRESHOLD;
        if (canComputer){
            huoYueService.save(list);
            log.info("插入了"+list.size()+"条数据");
        }else {
            int middle=(start+end)/2;
            List<ReachRate> leftList = list.subList(start, middle);//list的subList方法默认截取第一个参数到第二个参数的值，但不包含第二个参数
            List<ReachRate> rightList = list.subList(middle, end);
            HuoYueTask left=new HuoYueTask(leftList,huoYueService);
            HuoYueTask right=new HuoYueTask(rightList,huoYueService);
            invokeAll(left,right);
        }

    }

}
