package com.systec.noteapp;

import com.systec.noteapp.database.DataManager;
import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.pojos.NoteInfo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
//    static DataManager sDataManager;
//    @BeforeClass
//    public static  void classSetup(){
//        sDataManager = DataManager.getInstance();
//    }
//
//    @Before
//    public void setup(){
//
//        sDataManager.getNotes().clear();
//        sDataManager.initializeExampleNotes();
//    }
//
//    @Test
//    public void createNewNote() {
//        final CourseInfo course = sDataManager.getCourse("android_async");
//        final String noteTitle = "Test note title";
//        final String noteText = "This is the body text of my test note";
//
//        int noteIndex = sDataManager.createNewNote();
//        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);
//        newNote.setCourse(course);
//        newNote.setTitle(noteTitle);
//        newNote.setText(noteText);
//
//        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
//        assertEquals(course,compareNote.getCourse());
//        assertEquals(noteTitle,compareNote.getTitle());
//        assertEquals(noteText,compareNote.getText());
//
//    }
//
//    @Test
//    public void findSimilarNotes() {
//        final CourseInfo course = sDataManager.getCourse("android_async");
//        final String notetitle = "Test note title";
//        final String noteText = "This is the body text of my test note";
//        final String noteText2 = "This is the body of my second test";
//
//        int noteIndex = sDataManager.createNewNote();
//        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex);
//        newNote1.setCourse(course);
//        newNote1.setTitle(notetitle);
//        newNote1.setText(noteText);
//
//        int noteindes2 = sDataManager.createNewNote();
//        NoteInfo newNote2 = sDataManager.getNotes().get(noteindes2);
//        newNote2.setCourse(course);
//        newNote2.setTitle(notetitle);
//        newNote2.setText(noteText2);
//
//        int foundIndex1 = sDataManager.findNote(newNote1);
//        assertEquals(noteIndex, foundIndex1);
//
//        int foundIndex2 = sDataManager.findNote(newNote2);
//        assertEquals(noteindes2, foundIndex2);
//
//
//
//    }
//
//    @Test
//    public void createNewNoteOneStepCreations() {
//        final CourseInfo course = sDataManager.getCourse("android_async");
//        final String notetitle = "Test note title";
//        final String noteText = "This is the body text of my test note";
//
//        int noteIndex = sDataManager.createNewNote(course, notetitle, noteText);
//        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
//        assertEquals(course, compareNote.getCourse());
//        assertEquals(notetitle, compareNote.getTitle());
//        assertEquals(noteText, compareNote.getText());
//    }
    static DataManager sDataManager;

    @BeforeClass
    public static void classSetup(){
        sDataManager = DataManager.getInstance();

    }


    @Before
    public void setUp() throws Exception {
        sDataManager.getNotes().clear();
//        sDataManager.initializeExampleNotes();
    }


    @Test
    public void createNewNote() throws Exception {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note Title";
        final String noteText = "This is the body text of my text  note";

        int  noteIndex = sDataManager.createNewNote();
        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setText(noteText);
        newNote.setTitle(noteTitle);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(compareNote.getCourse(), course);
        assertEquals(compareNote.getText(), noteText);
        assertEquals(compareNote.getTitle(),noteTitle );
    }

    @Test
    public void findSimilarNote() throws Exception {

        DataManager sDataManager = DataManager.getInstance();
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String notetitle = "Test note title";
        final String noteText = "This is the body text of my text  note";
        final String noteText2 = "This is the body of my second test";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex);
        newNote1.setCourse(course);
        newNote1.setTitle(notetitle);
        newNote1.setText(noteText);

        int noteindes2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteindes2);
        newNote2.setCourse(course);
        newNote2.setTitle(notetitle);
        newNote2.setText(noteText2);

        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex, foundIndex1);

        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteindes2, foundIndex2);
        
    }

    @Test
    public void createNewNoteOneStopCreations() throws Exception {
        final CourseInfo courseInfo = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final  String noteText = "This is the body of my test note";

        int noteindex = sDataManager.createNewNote(courseInfo,noteTitle,noteText);
        NoteInfo compareNote = sDataManager.getNotes().get(noteindex);
        assertEquals(courseInfo, compareNote.getCourse());
        assertEquals(noteText, compareNote.getText());
        assertEquals(noteTitle, compareNote.getTitle());
    }
}