package com.systec.noteapp.ui.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.systec.noteapp.CourseEventBroadcastHelper;
import com.systec.noteapp.ModuleStatusView;
import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.database.DataManager;
import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.notifications.NoteReminderReceiver;
import com.systec.noteapp.R;
import com.systec.noteapp.ui.notes.NotesViewModel;

import java.util.Calendar;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private NotesViewModel mViewModel;
    public static final String NOTE_ID = "com.systec.noteapp.NOTE_ID";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.systec.noteapp.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.systec.noteapp.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.systec.noteapp.ORIGINAL_NOTE_TEXT";
    private final String TAG = getClass().getSimpleName();
    public static final int ID_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private Spinner mSpinnerCourses;
    private int mNOTE_ID;
    private boolean mIsCaceling;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;
    private boolean mSaved = false;
    private List<CourseInfo> mCourses;
    private ArrayAdapter<CourseInfo> mAdapterCourses;

    public static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private ModuleStatusView mViewModuleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        mViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);
//        mCourses = DataManager.getInstance().getCourses();

        mViewModel.getAllCourses().observe(this, new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                mCourses = courseInfos;
                setArrayAdapter(courseInfos);
            }
        });


        readDisplayStateValues();
        if (savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if (!mIsNewNote) {
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        }
        createNotificationChannel();

        mViewModuleStatus = (ModuleStatusView) findViewById(R.id.moduleStatusView);
        loadModuleStatusValues();


    }

    private void loadModuleStatusValues() {
        int totalNumberOfModules = 11;
        int completedNumberOfModule = 7;
        boolean[] moduleStatus = new boolean[totalNumberOfModules];
        for (int moduleIndex = 0; moduleIndex< completedNumberOfModule; moduleIndex++){
            moduleStatus[moduleIndex] = true;
        }
        mViewModuleStatus.setModuleStatus(moduleStatus);

    }

    private void setArrayAdapter(List<CourseInfo> courseInfos) {
//        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfos);
        mAdapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfos);
        mAdapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(mAdapterCourses);
        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    private void restoreOriginalNoteValues(Bundle bundle) {
        mOriginalNoteCourseId = bundle.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = bundle.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = bundle.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote) {
            return;
        }
        if (mNote != null) {
            mOriginalNoteCourseId = mNote.getCourse().getCourseId();
            mOriginalNoteTitle = mNote.getTitle();
            mOriginalNoteText = mNote.getText();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancle) {
            mIsCaceling = true;
            finish();
        } else if (id == R.id.action_next) {
            moveNext();
        } else if (id == R.id.action_save) {
            if (mIsNewNote) {
                saveNote();
            } else {
                UpdateNote();
            }
        }
        else if (id == R.id.action_set_reminder) {
            showReminderNotification();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showReminderNotification() {
        String noteText = mTextNoteText.getText().toString();
        String noteTitle = mTextNoteTitle.getText().toString();
        int noteId = mNote.getId();

        Intent intent = new Intent(this, NoteReminderReceiver.class);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_ID,noteId);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TEXT, noteText);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long currentTimeMilliseconds = SystemClock.elapsedRealtime();
        long ONE_HOUR = 60 * 60 * 1000;
        long TEN_SECONDS = 10 * 1000;
        long alarmTime = currentTimeMilliseconds + TEN_SECONDS;

        Snackbar.make(mTextNoteText,"Time Current " + currentTimeMilliseconds + " One hour : " + ONE_HOUR + " Ten Secs : " + TEN_SECONDS + " Alrm Time: " + alarmTime, (int) TEN_SECONDS).show();
        
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmTime,pendingIntent);

    }

    public void createNotificationChannel() {
        NotificationManager notifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
// Create a NotificationChannel

            NotificationChannel notificationChannel = new
                    NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            notifyManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        item.setEnabled(mNOTE_ID < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();

        ++mNOTE_ID;
        mNote = DataManager.getInstance().getNotes().get(mNOTE_ID);
        saveOriginalNoteValues();
        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I learned in the Pluralsight course \"" + course.getTitle() + "\"\n" + mTextNoteText.getText().toString();

        Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
        sendMailIntent.setType("message/rfc2822");
        sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendMailIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(sendMailIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCaceling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNOTE_ID);
            } else {
                storePreviousNoteValues();
            }
        } else {
            if (mIsNewNote) {
                saveNote();
            }

        }

    }

    private void UpdateNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
        NoteInfo note = new NoteInfo(mNote.getCourse().getCourseId(), mNote.getTitle(), mNote.getText());
        note.setId(mNote.getId());
        mViewModel.update(note);
        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    private void saveNote() {
        if (!mSaved) {
            int selectedPosition = mSpinnerCourses.getSelectedItemPosition();
            final CourseInfo course = mAdapterCourses.getItem(selectedPosition);

//            mNote.setTitle(mTextNoteTitle.getText().toString());
//            mNote.setText(mTextNoteText.getText().toString());
            NoteInfo note = new NoteInfo(course.getCourseId(), mTextNoteTitle.getText().toString(), mTextNoteText.getText().toString());
            mViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            mSaved = true;
        }
    }


    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
//        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        if (mNote != null) {
            CourseInfo course = mNote.getCourse();
            int courseIndex = mCourses.indexOf(course);
            String noteTitle = mNote.getTitle();
            String noteText = mNote.getText();

            spinnerCourses.setSelection(courseIndex);
            textNoteTitle.setText(noteTitle);
            textNoteText.setText(noteText);

            CourseEventBroadcastHelper.sendEventBroadCast(this,course.getCourseId(),"Editing Note");
        }

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNOTE_ID = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mIsNewNote = mNOTE_ID == ID_NOT_SET;
        if (mIsNewNote) {
            createNewNote();
        }

        if (!mIsNewNote) {
            mViewModel.loadNote(mNOTE_ID).observe(this, new Observer<NoteInfo>() {
                @Override
                public void onChanged(NoteInfo noteInfo) {
//                Log.d(TAG, "onChanged: " + noteInfo.getText());
                    mViewModel.getCourse(noteInfo.getCourseId()).observe(NoteActivity.this, new Observer<CourseInfo>() {
                        @Override
                        public void onChanged(CourseInfo courseInfo) {
                            mNote = new NoteInfo(noteInfo.getId(), courseInfo, noteInfo.getTitle(), noteInfo.getText());

                            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
                            Log.d(TAG, "onChanged: ViewModel" + mNote.getText());
                        }
                    });

                }
            });

        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNOTE_ID = dm.createNewNote();
//        mNote = dm.getNotes().get(mNOTE_ID);
    }
}
