package com.systec.noteapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "note_table")
public final class NoteInfo implements Parcelable
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private CourseInfo mCourse;
    private String CourseId;
    private String mTitle;
    private String mText;

    public NoteInfo(){}

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }
    public NoteInfo(String courseId, String title, String text) {
        CourseId = courseId;
        mTitle = title;
        mText = text;
    }

    public NoteInfo(int id, CourseInfo course, String courseId, String title, String text) {
        this.id = id;
        mCourse = course;
        CourseId = courseId;
        mTitle = title;
        mText = text;
    }

    private NoteInfo(Parcel parcel) {
        id = parcel.readInt();
        mCourse = parcel.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = parcel.readString();
        mText = parcel.readString();
    }

    public NoteInfo(int id, CourseInfo noteCourse, String title, String text) {
        mCourse = noteCourse;
        this.id = id;
        mTitle = title;
        mText = text;
    }


    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

//    private String getCompareKey() {
//        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        NoteInfo that = (NoteInfo) o;
//
//        return getCompareKey().equals(that.getCompareKey());
//    }

//    @Override
//    public int hashCode() {
//        return getCompareKey().hashCode();
//    }

//    @Override
//    public String toString() {
//        return getCompareKey();
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeParcelable(mCourse, 0);
        parcel.writeString(mTitle);
        parcel.writeString(mText);

    }

    public final static Parcelable.Creator<NoteInfo> CREATOR = new Creator<NoteInfo>() {
        @Override
        public NoteInfo createFromParcel(Parcel parcel) {
            return new NoteInfo(parcel);
        }

        @Override
        public NoteInfo[] newArray(int i) {
            return new NoteInfo[i];
        }
    };

}
