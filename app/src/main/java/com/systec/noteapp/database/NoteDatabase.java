package com.systec.noteapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.dao.CourseDao;
import com.systec.noteapp.dao.NoteDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NoteInfo.class, CourseInfo.class}, version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao mNoteDao();
    public abstract CourseDao mCourseDao();

    private static int NUMBER_OF_THREADS = 4;
    static  final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            PopulateDB();
        }

        private void PopulateDB() {
            NoteDao noteDao;
            CourseDao courseDao;
            courseDao = instance.mCourseDao();
            noteDao = instance.mNoteDao();
            databaseExecutor.execute(() -> {
                noteDao.insert(new NoteInfo("android_intents", "Dynamic intent resolution", "Wow, intents allow components to be resolved at runtime"));

                noteDao.insert(new NoteInfo("android_intents", "Delegating intents", "PendingIntents are powerful they delegate much more than just a component invocation")
                );

                noteDao.insert(new NoteInfo("android_async", "Service default threads", "Did you know that by default an Android Service will tie up the UI thread?"));

                noteDao.insert(new NoteInfo("android_async", "Long running operations", "Foreground Services can be tied to a notification icon")
                );

                noteDao.insert(new NoteInfo("java_lang", "Parameters", "Leverage variable-length parameter lists?")
                );

                noteDao.insert(new NoteInfo("java_lang", "Anonymous classes", "Anonymous classes simplify implementing one-use types")

                );

                noteDao.insert(new NoteInfo("java_core", "Compiler options", "The -jar option isn't compatible with with the -cp option")
                );

                noteDao.insert(new NoteInfo("java_core", "Serialization", "Remember to include SerialVersionUID to assure version compatibility")
                );


                courseDao.insert(new CourseInfo("android_intents", "Android Programming with Intents"));

                courseDao.insert(new CourseInfo("android_async", "Android Async Programming and Services"));

                courseDao.insert(new CourseInfo("java_lang", "Java Fundamentals: The Java Language"));

                courseDao.insert(new CourseInfo("java_core", "Java Fundamentals: The Core Platform"));

            });
        }
    };
}
