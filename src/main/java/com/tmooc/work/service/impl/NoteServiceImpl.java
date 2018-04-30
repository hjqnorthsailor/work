package com.tmooc.work.service.impl;

import com.tmooc.work.dao.NoteDao;
import com.tmooc.work.entity.Note;
import com.tmooc.work.entity.User;
import com.tmooc.work.service.NoteService;
import com.tmooc.work.util.FastDFSClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDao noteDao;
    @Override
    @Transactional
    public Note save(Note note, User user) {
        if (noteDao.existsByMonthAndWeekAndWeekDayAndUser(note.getMonth(),note.getWeek(),note.getWeekDay(),user.getUsername())){
            log.info("已经存在");
            Note note1 = noteDao.findNoteByMonthAndWeekAndWeekDayAndUser(note.getMonth(),note.getWeek(),note.getWeekDay(),user.getUsername());
            Integer id=note1.getId();
            BeanUtils.copyProperties(note, note1);//这里通过复制属性值的方式，不知是否影响效率
            note1.setId(id);//id不变，以起到更新的效果
            note.setUser(user.getUsername());//设置操作人
            return noteDao.saveAndFlush(note1);
        } else {
            note.setUser(user.getUsername());
            return noteDao.save(note);
        }

    }

    @Override
    public List<Note> findAll(Note note,User user) {
        return noteDao.findNotesByMonthAndWeekAndUser(note.getMonth(),note.getWeek(),user.getUsername());
    }
}
