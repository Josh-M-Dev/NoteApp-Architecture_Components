package com.systec.noteapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.systec.noteapp.pojos.NoteInfo;

import java.util.List;
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
@Dao
public interface NoteDao {
    @Insert
    void insert(NoteInfo noteInfo);

    @Update
    void update(NoteInfo noteInfo);

    @Delete
    void delete(NoteInfo noteInfo);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    LiveData<List<NoteInfo>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE id = :noteId LIMIT 1")
    LiveData<NoteInfo> loadNote(int noteId);

    @Query("DELETE FROM note_table WHERE id = :note_id")
    void  deleteWithID(int note_id);

    @Query("SELECT course_table.id AS courseTableID," +
            "course_table.mTitle AS courseTitle," +
            "course_table.CourseId AS CourseId," +
            "note_table.id AS noteTableID," +
            "note_table.mTitle AS noteTitle," +
            "note_table.mText AS noteText " +
            "FROM note_table,course_table " +
            "WHERE note_table.CourseId = course_table.CourseId" )
    LiveData<List<noteJoincourse>> loadNoteAndCourses();

    class noteJoincourse {
        public String CourseId;
        public int noteTableID;
        public String courseTitle;
        public String noteTitle;
        public String noteText;

        public noteJoincourse() {
        }

        public noteJoincourse(String courseId, int noteTableID, String courseTitle, String noteTitle, String noteText) {
            CourseId = courseId;
            this.noteTableID = noteTableID;
            this.courseTitle = courseTitle;
            this.noteTitle = noteTitle;
            this.noteText = noteText;
        }

        public String getCourseId() {
            return CourseId;
        }

        public void setCourseId(String courseId) {
            CourseId = courseId;
        }

        public int getNoteTableID() {
            return noteTableID;
        }

        public void setNoteTableID(int noteTableID) {
            this.noteTableID = noteTableID;
        }

        public String getCourseTitle() {
            return courseTitle;
        }

        public void setCourseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
        }

        public String getNoteTitle() {
            return noteTitle;
        }

        public void setNoteTitle(String noteTitle) {
            this.noteTitle = noteTitle;
        }

        public String getNoteText() {
            return noteText;
        }

        public void setNoteText(String noteText) {
            this.noteText = noteText;
        }
    }

}
