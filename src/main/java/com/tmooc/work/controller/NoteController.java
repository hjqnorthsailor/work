package com.tmooc.work.controller;

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
        Map<Integer,Object> dailyNote=new HashMap<>();
        notes.forEach(n->{if (n!=null)dailyNote.put(n.getWeekDay(),n);});
        Note note8 = getNote(notes);
        dailyNote.put(8,note8);
        return TmoocResult.ok(dailyNote);
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
        String userNotePath=myProperties.getExcelTemplatePath()+user.getUsername();//设置临时文件存储在/用户文件夹下
        File file=new File(userNotePath);
        if (!file.exists()){
            file.mkdir();
            log.info("创建了"+file);
        }
        String userFile=userNotePath+"/"+fileName;
        OutputStream os=new FileOutputStream(userNotePath+"/"+userFile);
        final List<Note> notes = noteService.findAll(note,user);
        Map<String,Object> noteMap=new HashMap<>();
        notes.forEach(n->noteMap.put("week"+n.getWeekDay().toString(),n));
        Note note8=getNote(notes);
        noteMap.put("week8",note8);
        JxlsUtils.exportExcel(templatePath, os, noteMap);
        os.close();
        JxlsUtils.doDownLoad(userFile,fileName,request.getResponse());
        return  TmoocResult.ok(userNotePath);
    }

    /**
     * 计算总值
     * @param notes
     * @return
     */
    private Note getNote(List<Note> notes) {
        int totalAnswer=0,totalRemote=0,totalCount=0,totalValidCount=0,totalBroadcast=0,totalInputTitle=0
                ,totalForward=0,totalOther=0;
        for (Note n : notes) {
            if (n.getAnswer()!=null){totalAnswer+=n.getAnswer();}
            if (n.getRemote()!=null){totalRemote+=n.getRemote();}
            if (n.getCount()!=null){totalCount+=n.getCount();}
            if (n.getValidCount()!=null){totalValidCount+=n.getValidCount();}
            if (n.getBroadcast()!=null){totalBroadcast+=n.getBroadcast();}
            if (n.getForward()!=null){totalForward+=n.getForward();}
            if (n.getInputTitle()!=null){totalInputTitle+=n.getInputTitle();}
            if (n.getOther()!=null){totalOther+=n.getOther();}
        }
        return Note.builder().answer(totalAnswer).remote(totalRemote).count(totalCount)
                .validCount(totalValidCount).broadcast(totalBroadcast).forward(totalForward)
                .inputTitle(totalInputTitle).other(totalOther).build();
    }
}
