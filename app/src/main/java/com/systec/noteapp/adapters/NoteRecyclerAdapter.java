package com.systec.noteapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.systec.noteapp.R;
import com.systec.noteapp.dao.NoteDao;
import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.ui.activity.NoteActivity;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.viewHolder> {
    private final Context mContext;
    private List<NoteDao.noteJoincourse> mNote = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private OnItemClickListner mListner;
    private static final String TAG = "NoteRecyclerAdapter";
    private CourseInfo mNoteCourse;

    public NoteRecyclerAdapter(Context context) {
        mContext = context;
//        mNote = note;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            if (mNote!=null){
                NoteDao.noteJoincourse note = mNote.get(position);
                holder.mTextNote.setText(note.getNoteTitle());
                holder.mTextCourse.setText(note.getCourseTitle());
                holder.mCurrentPosition = position;
            }
            else {
                Toast.makeText(mContext, "DataFetching", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    public int getItemCount() {
        if (mNote!=null){
            return mNote.size();
        }
        return 0;
    }

    public void setNotes(List<CourseInfo> course, List<NoteInfo> notes) {
        for (NoteInfo noteInfo : notes) {
            for (CourseInfo courseInfo : course) {
                if (noteInfo.getCourseId().equals(courseInfo.getCourseId())) {

                    mNoteCourse = courseInfo;
                }
            }
//            mNote.add(new NoteInfo(noteInfo.getId(), mNoteCourse, noteInfo.getTitle(), noteInfo.getText()));

        }
        notifyDataSetChanged();
    }

    public void setNotes(List<NoteDao.noteJoincourse> noteJoincourses) {
        mNote = noteJoincourses;
        notifyDataSetChanged();

    }

    public class viewHolder extends RecyclerView.ViewHolder {


        public final TextView mTextCourse;
        public final TextView mTextNote;
        public int mCurrentPosition;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);
            mTextNote = itemView.findViewById(R.id.text_title);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if (mListner != null && position != RecyclerView.NO_POSITION) {
//
//                        mListner.onItemClick(mNote.get(position));
//                    }
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    int id = mNote.get(mCurrentPosition).getNoteTableID();
                    intent.putExtra(NoteActivity.NOTE_ID, id);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("note_id",mNote.get(mCurrentPosition).getId());

                    mContext.startActivity(intent);
                }
            });

        }
    }

    public NoteDao.noteJoincourse getNoteAt(int position){
        return  mNote.get(position);
    }

    public interface OnItemClickListner {
        void onItemClick(NoteInfo note);
    }

//    public void setOnItemClickListener(OnItemClickListner listener) {
//        this.mListner = listener;
//    }
}
