package com.tmooc.work.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.alibaba.druid.sql.visitor.functions.If;
import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.dao.NoteDao;
import com.tmooc.work.entity.Note;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.NoteService;
import com.tmooc.work.util.FastDFSClientWrapper;
import org.apache.http.util.Asserts;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;
    @RequestMapping("save")
    public TmoocResult save(Note note, User user){
        note.setUser(user.getUsername());
        final Note n = noteService.save(note,user);
        return TmoocResult.ok(n);
    }
    @RequestMapping("findAll")
    public TmoocResult findAll(Note note,User user){
        final List<Note> notes = noteService.findAll(note,user);
        Map<Integer,Object> dailyNote=new HashMap<>();
        notes.forEach(n->{if (n!=null)dailyNote.put(n.getWeekDay(),n);});
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
        Note note8=Note.builder().answer(totalAnswer).remote(totalRemote).count(totalCount)
                .validCount(totalValidCount).broadcast(totalBroadcast).forward(totalForward)
                .inputTitle(totalInputTitle).other(totalOther).build();
        dailyNote.put(8,note8);
        return TmoocResult.ok(dailyNote);
    }
    @RequestMapping("download")
    public TmoocResult dowloadExcel(Note note,User user) throws IOException {
        final List<Note> notes = noteService.findAll(note,user);
        System.out.println(notes.size());
        Map<String,Object> dailyNote=new HashMap<>();
        notes.forEach(n->dailyNote.put("week"+n.getWeekDay().toString(),n));
        dailyNote.forEach((k,v)-> System.out.println(k+","+v));
        TemplateExportParams params=new TemplateExportParams("E:\\git\\work\\src\\main\\resources\\templates\\excel\\note.xlsx",true);
        params.setColForEach(true);
        Workbook book = ExcelExportUtil.exportExcel(params, dailyNote);
        FileOutputStream fos=null;
        String pathName="E:\\git\\work\\src\\main\\resources\\templates\\excel\\note1.xlsx";
        String filePath=null;
        try {
            fos = new FileOutputStream(pathName);
            book.write(fos);
            fos.close();
            System.out.println("成功");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fos.close();
//            filePath = uploadExcel(pathName);
        }
        return TmoocResult.ok(filePath);
    }
    public String uploadExcel(String pathName) throws IOException {
        File file = new File(pathName);
        FileInputStream in_file = new FileInputStream(file);
        // 转 MultipartFile
        MultipartFile multi = new MockMultipartFile("test.xlsx",in_file);
        String uploadFile = fastDFSClientWrapper.uploadFile(multi);
        return uploadFile;
    }
}
