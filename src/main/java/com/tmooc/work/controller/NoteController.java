package com.tmooc.work.controller;

import com.tmooc.work.common.TmoocResult;
import com.tmooc.work.dao.NoteDao;
import com.tmooc.work.entity.Note;
import com.tmooc.work.service.NoteService;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @RequestMapping("save")
    public TmoocResult save(Note note){
        final Note n = noteService.save(note);
        return TmoocResult.ok(n);
    }
    @RequestMapping("findAll")
    public TmoocResult findAll(Note note){
        final List<Note> notes = noteService.findAll(note);
        Map<Integer,Note> dailyNote=new HashMap<>();
        notes.forEach(n->dailyNote.put(n.getWeekDay(),n));

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
}
