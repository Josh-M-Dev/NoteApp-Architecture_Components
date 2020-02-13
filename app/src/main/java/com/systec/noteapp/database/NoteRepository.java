package com.systec.noteapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.dao.CourseDao;
import com.systec.noteapp.dao.NoteDao;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private CourseDao mCourseDao;

    private LiveData<List<NoteInfo>> allNotes;

    public static  NoteRepository instance = null;



    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        mNoteDao = database.mNoteDao();
        allNotes = mNoteDao.getAllNotes();

    }

    public void insert(final NoteInfo noteInfo) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mNoteDao.insert(noteInfo);
        });
    }

    public void update(NoteInfo noteInfo) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mNoteDao.update(noteInfo);
        });
    }

    public void deleteAllNotes() {
        NoteDatabase.databaseExecutor.execute(() ->{
            mNoteDao.deleteAllNotes();
        });
    }

    public void delete(NoteInfo noteInfo) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mNoteDao.delete(noteInfo);
        });
    }

    public LiveData<List<NoteInfo>> getAllNotes() {
        return allNotes;
    }

    public LiveData<NoteInfo> loadNote(int id){
        return mNoteDao.loadNote(id);
    }
    public LiveData<List<NoteDao.noteJoincourse>> mData(){
        return mNoteDao.loadNoteAndCourses();
    }

    public void deleteWithID(int noteInfo) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mNoteDao.deleteWithID(noteInfo);
        });

    }


//
//    public LiveData<CourseInfo> getCoursetitle(String title) {
//        return mNoteDao.getCourseTitle(title);
//    }
}
