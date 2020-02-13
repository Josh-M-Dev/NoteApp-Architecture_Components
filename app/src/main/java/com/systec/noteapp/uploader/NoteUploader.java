package com.systec.noteapp.uploader;

import android.content.Context;
import android.util.Log;

import com.systec.noteapp.dao.NoteDao;
import com.systec.noteapp.database.NoteRepository;

import java.net.ContentHandler;
import java.util.List;


/**
 * Created by Jim.
 */

public class NoteUploader {
    private static final String TAG = "NoteUploader";

    private final Context mContext;
    private boolean mCanceled;

    public NoteUploader(Context context){
        mContext = context;
    }

    public boolean isCanceled() {
        return mCanceled;
    }
    public  void canceled(){
        mCanceled = true;
    }

    public static void doUpload(List<NoteDao.noteJoincourse> columns) {

        Log.i(TAG, ">>>***   UPLOAD START - Thread: " + Thread.currentThread().getId() + "   ***<<<");
        for (NoteDao.noteJoincourse joincourse : columns){
            String courseId = joincourse.getCourseId();
            String courseTitle = joincourse.getCourseTitle();
            String noteTitle = joincourse.getNoteTitle();
            String noteText = joincourse.getNoteText();

            if(!noteTitle.equals("")) {
                Log.i(TAG, ">>>Uploading Note<<< " + courseId + "|" + courseTitle + "|" + noteTitle + "|" + noteText);
                simulateLongRunningWork();
            }
        }
        Log.i(TAG, ">>>***   UPLOAD COMPLETE   ***<<<");
    }


    private static void simulateLongRunningWork() {
        try {
            Thread.sleep(5000);
        } catch(Exception ex) {}
    }


}
