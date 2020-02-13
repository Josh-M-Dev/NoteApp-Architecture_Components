package com.systec.noteapp.ui.courses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.database.CourseRepository;

import java.util.List;

public class CoursesViewModel extends AndroidViewModel {

    private LiveData<List<CourseInfo>> mCourses;
    private CourseRepository mCourseRepository;


    public CoursesViewModel(@NonNull Application application) {
        super(application);
        mCourseRepository = new CourseRepository(application);
        mCourses = mCourseRepository.getAllCourses();

    }

    public void insert(CourseInfo course) {
       mCourseRepository.insert(course);
    }

    public void update(CourseInfo course) {
        mCourseRepository.update(course);
    }

    public void deleteAllCourses() {
        mCourseRepository.deleteAllCourses();
    }

    public void delete(CourseInfo course) {
        mCourseRepository.delete(course);
    }
    public LiveData<CourseInfo> loadCourse(String id) {
        return mCourseRepository.loadCourse(id);
    }

    public LiveData<List<CourseInfo>> getAllCourses() {
        return mCourses;
    }
}