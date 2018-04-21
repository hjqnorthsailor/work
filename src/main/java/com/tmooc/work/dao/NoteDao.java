package com.tmooc.work.dao;

import com.tmooc.work.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NoteDao extends JpaRepository<Note,Integer> {
    boolean existsByMonthAndWeekAndWeekDay(Integer month,Integer week,Integer weekDay);
    Note findNoteByMonthAndWeekAndWeekDay(Integer month,Integer week,Integer weekDay);
    List<Note> findNotesByMonthAndWeek(Integer month,Integer week);
}
