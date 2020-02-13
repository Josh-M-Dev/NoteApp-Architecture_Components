package com.systec.noteapp;

import androidx.test.espresso.contrib.AccessibilityChecks;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheck;
import com.systec.noteapp.database.DataManager;
import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.ui.activity.MainActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class NoteListActivityTest {
    static DataManager sDataManager;
    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void classSetup(){
        sDataManager = DataManager.getInstance();
    }
    @BeforeClass
    public static void turnOnAccessibility(){
        AccessibilityChecks.enable();
    }


    @Test
    public void createNewNote() throws Exception {
        final String noteText = "This is the body of our text note";
        final String note_title = "Text note title";

        onView(withId(R.id.fab)).perform(click());


        onView(withId(R.id.text_note_title)).perform(typeText(note_title)).check(matches(withText(containsString(note_title))));
        onView(withId(R.id.text_note_text)).perform(typeText(noteText),
        closeSoftKeyboard());
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

        pressBack();
    }
}