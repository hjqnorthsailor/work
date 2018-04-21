package com.tmooc.work.service;

import com.tmooc.work.entity.Note;

import java.util.List;

public interface NoteService {
    Note save(Note note);
    List<Note> findAll(Note note);
}
