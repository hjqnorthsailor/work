package com.tmooc.work.batch;

import com.tmooc.work.entity.Student;
import com.tmooc.work.service.StudentService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.RecursiveAction;
@Slf4j
public class StudentTask extends RecursiveAction {
    private static final int THRESHOLD=100;
    private int start;
    private int end;
    private List<Student> list;
    private StudentService studentService;
    public StudentTask(List<Student> list,StudentService studentService){
        this.start=0;
        this.end=list.size();
        this.list=list;
        this.studentService=studentService;
    }
    @Override
    protected void compute() {
        boolean canComputer=(end-start)<=THRESHOLD;
        if (canComputer){
            studentService.save(list);
            log.info("插入了"+list.size()+"条数据");
        }else {
            int middle=(start+end)/2;
            List<Student> leftList = list.subList(start, middle);//list的subList方法默认截取第一个参数到第二个参数的值，但不包含第二个参数
            List<Student> rightList = list.subList(middle, end);
            StudentTask left=new StudentTask(leftList,studentService);
            StudentTask right=new StudentTask(rightList,studentService);
            invokeAll(left,right);
        }

    }

}
