package com.systec.noteapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.systec.noteapp.pojos.CourseInfo;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(CourseInfo courseInfo);

    @Update
    void update(CourseInfo courseInfo);

    @Delete
    void delete(CourseInfo courseInfo);

    @Query("DELETE FROM course_table")
    void  deleteAlCourses();

    @Query("SELECT * FROM course_table ORDER BY mTitle DESC ")
    LiveData<List<CourseInfo>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE CourseId = :id")
    LiveData<CourseInfo> loadCourse(String id);


}
