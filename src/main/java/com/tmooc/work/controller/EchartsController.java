package com.tmooc.work.controller;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Gauge;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.google.common.collect.Lists;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.enums.StudentStage;
import com.tmooc.work.service.ReachService;
import com.tmooc.work.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author northsailor
 */
@RestController
@Slf4j
public class EchartsController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private ReachService reachService;
    @RequestMapping("/echarts/bar")
    public TmoocResult getBar(){
        final List<Map<String, Object>> studentsInfo = studentService.findStudentsInfo();
        Option option=new Option();
        option.title("已跟进人数").tooltip(Trigger.axis).legend("人数（人）");
        //横轴为值轴
        option.yAxis(new ValueAxis().boundaryGap(0d, 0.01));
        //创建类目轴
        CategoryAxis category = new CategoryAxis();
        //柱状数据
        Bar bar = new Bar("人数（人）");
        studentsInfo.forEach(s->{
            category.data(s.get("qunName"));
            bar.data(s.get("countNum"));

        });
        bar.animation(true);
        //设置类目轴
        option.xAxis(category);
        //设置数据
        option.series(bar);
        return TmoocResult.ok(option);
    }
    @RequestMapping("/echarts/gauge")
    public TmoocResult getGauge(){
        Option option=new Option();
        option.title("跟进率");
        option.tooltip().formatter("{a} <br/>{b} : {c}%");
        option.toolbox().show(true).feature(Tool.mark, Tool.restore, Tool.saveAsImage);
        Gauge gauge=new Gauge();
        gauge.max(studentService.findAll().size());
        gauge.min(0);
        gauge.data(studentService.countAllByStage(StudentStage.FOLLOWUP.getStage()));
        gauge.splitNumber(10);
        option.series(gauge);
        return TmoocResult.ok(option);
    }
    @RequestMapping("/echarts/markBar")
    public TmoocResult getMarkBar(){
        final List<Map<String, Object>> studentsInfo = studentService.findStudentsByMark();
        Option option=new Option();
        option.title("学员类别分布").tooltip(Trigger.axis).legend("人数（人）");
        //横轴为值轴
        option.yAxis(new ValueAxis().boundaryGap(0d, 0.01));
        //创建类目轴
        CategoryAxis category = new CategoryAxis();
        //柱状数据
        Bar bar = new Bar("人数（人）");
        studentsInfo.forEach(s->{
            if (null!=s.get("mark")) {
                category.data(s.get("mark"));
                bar.data(s.get("value"));
            }

        });
        bar.animation(true);
        //设置类目轴
        option.xAxis(category);
        //设置数据
        option.series(bar);
        return TmoocResult.ok(option);
    }
    @RequestMapping("/echarts/lines")
    public TmoocResult getLines(){
        Option option=new Option();
        option.title("QQ群达到率");
        option.tooltip().trigger(Trigger.axis);
        List<Map<String, Object>> GroupReaches = reachService.findAllGroupReaches();
        Set<String> dates=new HashSet<>();
        GroupReaches.forEach(g->dates.add(g.get("date").toString()));
        List<String> dateList=new ArrayList<>(dates);
        Collections.sort(dateList, (o1, o2) -> {
            if(Double.parseDouble(o1)>Double.parseDouble(o2)){
                return 1;
            }
            return -1;
        });
        log.info(Arrays.toString(dateList.toArray()));
        option.xAxis(new CategoryAxis().boundaryGap(false).data(dateList.toArray()));
        option.yAxis(new ValueAxis());
        Set<String> qunNames=new HashSet<>();
        GroupReaches.forEach(g->qunNames.add(g.get("qunName").toString()));
        List<Series> seriesList=Lists.newArrayList();
        qunNames.forEach(q->{
            option.legend(q);
            Line line=new Line(q);
            line.smooth(true).itemStyle().normal().lineStyle();
            GroupReaches.forEach(g->{
                if (g.containsKey(q+g.get("date"))){
                line.data(g.get(q+g.get("date")));
                }
            });
                seriesList.add(line);
            });
        option.series(seriesList);
        return TmoocResult.ok(option);

    }

}
