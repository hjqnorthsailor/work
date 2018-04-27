package com.tmooc.work.dao;

import com.tmooc.work.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NoteDao extends JpaRepository<Note,Integer> {
    boolean existsByMonthAndWeekAndWeekDayAndUser(Integer month,Integer week,Integer weekDay,String user);
    Note findNoteByMonthAndWeekAndWeekDayAndUser(Integer month,Integer week,Integer weekDay,String user);
    List<Note> findNotesByMonthAndWeekAndUser(Integer month,Integer week,String user);
}
