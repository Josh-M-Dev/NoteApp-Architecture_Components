package com.systec.noteapp.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.adapters.CourseRecyclerAdapter;
import com.systec.noteapp.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    private CoursesViewModel mCoursesViewModel;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private View mRoot;
    private RecyclerView mRecyclerItems;
    private GridLayoutManager mCoursesLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCoursesViewModel =
                ViewModelProviders.of(this).get(CoursesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_courses, container, false);
        mCoursesViewModel.getAllCourses().observe(this, new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                mCourseRecyclerAdapter.setData(courseInfos);

            }
        });

        initializeDisplayContent();

        return this.mRoot;
    }
    private void initializeDisplayContent() {

        mRecyclerItems = mRoot.findViewById(R.id.list_items);
        mCoursesLayoutManager = new GridLayoutManager(getContext(),getResources().getInteger(R.integer.course_grid_span));
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);


        mCourseRecyclerAdapter = new CourseRecyclerAdapter(getContext());

        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        mCourseRecyclerAdapter.notifyDataSetChanged();
    }
}