package com.tmooc.work.service.impl;

import com.tmooc.work.dao.NoteDao;
import com.tmooc.work.entity.Note;
import com.tmooc.work.service.NoteService;
import com.tmooc.work.util.FastDFSClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDao noteDao;
    @Override
    public Note save(Note note) {
        if (noteDao.existsByMonthAndWeekAndWeekDay(note.getMonth(),note.getWeek(),note.getWeekDay())){
            log.info("已经存在");
            Note note1 = noteDao.findNoteByMonthAndWeekAndWeekDay(note.getMonth(),note.getWeek(),note.getWeekDay());
            Integer id=note1.getId();
            BeanUtils.copyProperties(note, note1);//这里通过复制属性值的方式，不知是否影响效率
            note1.setId(id);//id不变，以起到更新的效果
            return noteDao.saveAndFlush(note1);
        } else {
            return noteDao.save(note);
        }

    }

    @Override
    public List<Note> findAll(Note note) {
        return noteDao.findNotesByMonthAndWeek(note.getMonth(),note.getWeek());
    }
}
