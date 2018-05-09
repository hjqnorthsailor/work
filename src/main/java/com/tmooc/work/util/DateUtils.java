package com.tmooc.work.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {
    public static boolean isBetweenTime(Date target,Date start,Date end){
        if (target.getTime()==start.getTime()||target.getTime()==end.getTime()){
            return true;
        }
        Calendar targetDate=Calendar.getInstance();
        targetDate.setTime(target);
        Calendar startDate=Calendar.getInstance();
        startDate.setTime(start);
        Calendar endDate=Calendar.getInstance();
        endDate.setTime(end);
        if (targetDate.after(startDate)&&targetDate.before(endDate)){
            return true;
        }else {
            return false;
        }

    }

    public static void main(String[] args) throws ParseException {
        String startTime="2018-05-08";
        String endTime="2018-05-09";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start=simpleDateFormat.parse(startTime.trim()+" 00:00:00");
        Date end=simpleDateFormat.parse(endTime.trim()+" 23:59:59");
        System.out.println(isBetweenTime(new Date(),start,end));
    }
}
