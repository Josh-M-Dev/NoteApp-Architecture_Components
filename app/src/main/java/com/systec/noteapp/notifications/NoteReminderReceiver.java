package com.systec.noteapp.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.systec.noteapp.ui.activity.NoteActivity;

public class NoteReminderReceiver extends BroadcastReceiver {
    public static final String EXTRA_NOTE_TITLE = "com.systec.noteapp.extra.NOTE_TITLE";
    public static final String EXTRA_NOTE_TEXT = "com.systec.noteapp.extra.NOTE_TEXT";
    public static final String EXTRA_NOTE_ID = "com.systec.noteapp.extra.NOTE_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        String notetitle = intent.getStringExtra(EXTRA_NOTE_TITLE);
        String noteText = intent.getStringExtra(EXTRA_NOTE_TEXT);
        int noteId = intent.getIntExtra(EXTRA_NOTE_ID, -NoteActivity.ID_NOT_SET);

        NoteReminderNotification.notify(context, notetitle, noteText, noteId, NoteActivity.PRIMARY_CHANNEL_ID);

    }
}
