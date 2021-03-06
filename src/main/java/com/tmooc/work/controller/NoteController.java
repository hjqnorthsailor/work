package com.tmooc.work.controller;

import com.google.common.collect.Maps;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.config.MyProperties;
import com.tmooc.work.entity.Note;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.NoteService;
import com.tmooc.work.util.JxlsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/note")
@Slf4j
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private MyProperties myProperties;
    @RequestMapping("save")
    public TmoocResult save(Note note, User user){
        note.setUser(user.getUsername());
        final Note n = noteService.save(note,user);
        return TmoocResult.ok(n);
    }

    /**
     * 获取对应用户的周报内容
     * @param note
     * @param user
     * @return
     */
    @RequestMapping("findAll")
    public TmoocResult findAll(Note note,User user){
        final List<Note> notes = noteService.findAll(note,user);
        if (notes.size()>0) {
            Map<Integer, Object> dailyNote = Maps.newHashMap();
            notes.forEach(n -> {
                if (n != null) {
                    dailyNote.put(n.getWeekDay(), n);
                }
            });
            Note note8 = getNote(notes);
            dailyNote.put(8, note8);
            return TmoocResult.ok(dailyNote);
        }
        return TmoocResult.error("你还没有写周报");
    }

    /**
     * 导入周报数据到模板上并下载
     * @param note
     * @param
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("download")
    public TmoocResult writeExcel(Note note, ServletWebRequest request, User user) throws Exception {
        System.out.println(request.getRequest().getContextPath());
        String templatePath=myProperties.getExcelTemplatePath()+"note.xlsx";
        String fileName=user.getUsername()+"_"+note.getMonth()+"_"+note.getWeek()+".xlsx";
        //设置临时文件存储在/用户文件夹下
        String userNotePath=myProperties.getExcelTemplatePath()+user.getUsername();
        File file=new File(userNotePath);
        if (!file.exists()){
            file.mkdir();
            log.info("创建了"+file);
        }
        String userFile=userNotePath+"/"+fileName;
        OutputStream os=new FileOutputStream(userFile);
        final List<Note> notes = noteService.findAll(note,user);
        Map<String,Object> noteMap=new HashMap<>();
        notes.forEach(n->noteMap.put("week"+n.getWeekDay().toString(),n));
        Note note8=getNote(notes);
        noteMap.put("week8",note8);
        JxlsUtils.exportExcel(templatePath, os, noteMap);
        os.close();
        JxlsUtils.doDownLoad(userFile,fileName,request.getResponse());
        return  TmoocResult.ok(userFile);
    }

    /**
     * 计算总值
     * @param notes
     * @return
     */
    private Note getNote(final List<Note> notes) {
        AtomicInteger totalAnswer=new AtomicInteger(0);
        AtomicInteger totalRemote=new AtomicInteger(0);
        AtomicInteger totalCount=new AtomicInteger(0);
        AtomicInteger totalValidCount=new AtomicInteger(0);
        AtomicInteger totalBroadcast=new AtomicInteger(0);
        AtomicInteger totalInputTitle=new AtomicInteger(0);
        AtomicInteger totalForward=new AtomicInteger(0);
        AtomicInteger totalOther=new AtomicInteger(0);
        for (Note n : notes) {
            if (n.getAnswer()!=null){totalAnswer.addAndGet(n.getAnswer());}
            if (n.getRemote()!=null){totalRemote.addAndGet(n.getRemote());}
            if (n.getCount()!=null){totalCount.addAndGet(n.getCount());}
            if (n.getValidCount()!=null){totalValidCount.addAndGet(n.getValidCount());}
            if (n.getBroadcast()!=null){totalBroadcast.addAndGet(n.getBroadcast());}
            if (n.getForward()!=null){totalForward.addAndGet(n.getForward());}
            if (n.getInputTitle()!=null){totalInputTitle.addAndGet(n.getInputTitle());}
            if (n.getOther()!=null){totalOther.addAndGet(n.getOther());}
        }
        return Note.builder().answer(totalAnswer.get()).remote(totalRemote.get()).count(totalCount.get())
                .validCount(totalValidCount.get()).broadcast(totalBroadcast.get()).forward(totalForward.get())
                .inputTitle(totalInputTitle.get()).other(totalOther.get()).build();
    }
}
