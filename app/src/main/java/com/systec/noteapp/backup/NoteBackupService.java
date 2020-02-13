package com.systec.noteapp.backup;

import android.app.IntentService;
import android.content.Intent;

import com.systec.noteapp.database.DataManager;

public class NoteBackupService extends IntentService {
   public static final String EXTRA_COURSE_ID = "com.systec.noteapp.extra.COURSE_ID";

    public NoteBackupService() {
        super("NoteBackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String backupCourseId = intent.getStringExtra(EXTRA_COURSE_ID);
            NoteBackup.doBackup(DataManager.getInstance().getData);

        }
    }

}
