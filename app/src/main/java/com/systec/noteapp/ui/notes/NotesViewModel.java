package com.systec.noteapp.ui.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.pojos.CourseInfo;
//import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.dao.NoteDao;
import com.systec.noteapp.database.CourseRepository;
import com.systec.noteapp.database.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private CourseRepository mCourseRepository;
    private LiveData<List<NoteInfo>> allNotes;
    private LiveData<List<CourseInfo>> allCourses;


    public  List<NoteInfo> mNote = new ArrayList<>();
    public  List<CourseInfo> mCourse = new ArrayList<>();

    public NotesViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mCourseRepository = new CourseRepository(application);
        allCourses = mCourseRepository.getAllCourses();
        allNotes = mNoteRepository.getAllNotes();
    }

    public void insert(NoteInfo noteInfo) {
        mNoteRepository.insert(noteInfo);
    }

    public void update(NoteInfo noteInfo) {
        mNoteRepository.update(noteInfo);
    }

    public void delete(NoteDao.noteJoincourse noteInfo) {
        mNoteRepository.deleteWithID(noteInfo.getNoteTableID());
    }

    public void deleteAllNotes() {
        mNoteRepository.deleteAllNotes();
    }

    public LiveData<List<CourseInfo>> getAllCourses(){
        return allCourses;
    }
    LiveData<List<NoteInfo>> getAllNotes(){
        return allNotes;
    }


    public LiveData<NoteInfo> loadNote(int id){
        return mNoteRepository.loadNote(id);
    }
    public LiveData<CourseInfo> getCourse(String id){
        return mCourseRepository.loadCourse(id);
    }
    public LiveData<List<NoteDao.noteJoincourse>> mData(){
        return mNoteRepository.mData();
    }

}