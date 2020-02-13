package com.systec.noteapp.backup;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.systec.noteapp.dao.NoteDao;
import com.systec.noteapp.database.NoteRepository;
import com.systec.noteapp.ui.notes.NotesViewModel;

import java.util.List;


/**
 * Created by Jim.
 */

public class NoteBackup {
    public static final String ALL_COURSES = "ALL_COURSES";
    private static final String TAG = "NoteBackup";
    private NoteRepository mNoteRepository;
//    private static List<NoteDao.noteJoincourse> columns;



    public static void doBackup(List<NoteDao.noteJoincourse> columns) {

        Log.i(TAG, ">>>***   BACKUP START - Thread: " + Thread.currentThread().getId() + "   ***<<<");
        for (NoteDao.noteJoincourse joincourse : columns){
            String courseId = joincourse.getCourseId();
            String courseTitle = joincourse.getCourseTitle();
            String noteTitle = joincourse.getNoteTitle();
            String noteText = joincourse.getNoteText();

            if(!noteTitle.equals("")) {
                Log.i(TAG, ">>>Backing Up Note<<< " + courseId + "|" + courseTitle + "|" + noteTitle + "|" + noteText);
                simulateLongRunningWork();
            }
        }
        Log.i(TAG, ">>>***   BACKUP COMPLETE   ***<<<");
    }


    private static void simulateLongRunningWork() {
        try {
            Thread.sleep(5000);
        } catch(Exception ex) {}
    }


}
