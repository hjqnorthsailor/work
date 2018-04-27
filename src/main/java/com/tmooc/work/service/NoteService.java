package com.tmooc.work.service;

import com.tmooc.work.entity.Note;
import com.tmooc.work.entity.User;

import java.util.List;

public interface NoteService {
    Note save(Note note, User user);
    List<Note> findAll(Note note,User user);
}
