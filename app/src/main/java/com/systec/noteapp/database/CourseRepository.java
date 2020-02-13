package com.systec.noteapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.dao.CourseDao;

import java.util.List;

public class CourseRepository {
    private CourseDao mCourseDao;
    private LiveData<List<CourseInfo>> allCourses;

    public CourseRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        mCourseDao = database.mCourseDao();
        allCourses = mCourseDao.getAllCourses();
    }

    public void insert(CourseInfo course) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mCourseDao.insert(course);
        });
    }

    public void update(CourseInfo course) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mCourseDao.update(course);
        });
    }

    public void deleteAllCourses() {
        NoteDatabase.databaseExecutor.execute(() ->{
            mCourseDao.deleteAlCourses();
        });
    }

    public void delete(CourseInfo course) {
        NoteDatabase.databaseExecutor.execute(() ->{
            mCourseDao.delete(course);
        });
    }
    public LiveData<CourseInfo> loadCourse(String id) {
        return mCourseDao.loadCourse(id);
    }

    public LiveData<List<CourseInfo>> getAllCourses() {
        return allCourses;
    }
}
