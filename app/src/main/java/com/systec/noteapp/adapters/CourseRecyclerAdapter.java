package com.systec.noteapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.systec.noteapp.R;
import com.systec.noteapp.pojos.CourseInfo;

import java.util.List;

public class CourseRecyclerAdapter extends  RecyclerView.Adapter<CourseRecyclerAdapter.viewHolder> {
    private  final Context mContext;
    private List<CourseInfo> mCourse;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context context) {
        mContext = context;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CourseInfo course = mCourse.get(position);
        holder.mTextCourse.setText(course.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        if (mCourse!=null){
            return mCourse.size();
        }
        return 0;
    }

    public void setData(List<CourseInfo> courseInfos) {
        mCourse = courseInfos;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder{


        public final TextView mTextCourse;

        public int mCurrentPosition;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, mCourse.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }
}
