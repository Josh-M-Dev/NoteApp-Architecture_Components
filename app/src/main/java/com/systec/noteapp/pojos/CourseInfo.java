package com.systec.noteapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "course_table")
public final class CourseInfo implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String CourseId;
    private String mTitle;
    @Ignore
    private List<ModuleInfo> mModules;

    public CourseInfo(){}

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public CourseInfo(String courseId, String title, List<ModuleInfo> modules) {
        CourseId = courseId;
        mTitle = title;
        mModules = modules;
    }

    public CourseInfo(String courseId, String title) {
        CourseId = courseId;
        mTitle = title;
    }

    public CourseInfo(Parcel parcel) {
        id = parcel.readInt();
        CourseId = parcel.readString();
        mTitle = parcel.readString();
        mModules = new ArrayList<>();
        parcel.readTypedList(mModules, ModuleInfo.CREATOR);
    }

    public void setModules(List<ModuleInfo> modules) {
        mModules = modules;
    }

    public String getCourseId() {
        return CourseId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<ModuleInfo> getModules() {
        return mModules;
    }

    public boolean[] getModulesCompletionStatus() {
        boolean[] status = new boolean[mModules.size()];

        for(int i=0; i < mModules.size(); i++)
            status[i] = mModules.get(i).isComplete();

        return status;
    }

    public void setModulesCompletionStatus(boolean[] status) {
        for(int i=0; i < mModules.size(); i++)
            mModules.get(i).setComplete(status[i]);
    }

    public ModuleInfo getModule(String moduleId) {
        for(ModuleInfo moduleInfo: mModules) {
            if(moduleId.equals(moduleInfo.getModuleId()))
                return moduleInfo;
        }
        return null;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseInfo that = (CourseInfo) o;

        return CourseId.equals(that.CourseId);

    }

    @Override
    public int hashCode() {
        return CourseId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(CourseId);
        parcel.writeString(mTitle);
        parcel.writeTypedList(mModules);

    }
    public final  static  Parcelable.Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel parcel) {
            return new CourseInfo(parcel);
        }

        @Override
        public CourseInfo[] newArray(int i) {
            return new CourseInfo[i];
        }
    };
}
