package com.systec.noteapp.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.systec.noteapp.pojos.CourseInfo;
import com.systec.noteapp.database.DataManager;
import com.systec.noteapp.ui.activity.NoteActivity;
import com.systec.noteapp.pojos.NoteInfo;
import com.systec.noteapp.adapters.NoteRecyclerAdapter;
import com.systec.noteapp.R;
import com.systec.noteapp.dao.NoteDao;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private NotesViewModel mNotesViewModel;
    private List<NoteInfo> noteInfo = new ArrayList<>();
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private View mRoot;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mNotesLayoutManager;
    private static final String TAG = "NotesFragment";
    private final List<CourseInfo> mDbCourseList = new ArrayList<>();
    private final List<NoteInfo> mNote = new ArrayList<>();
    private List<CourseInfo> mCourse = new ArrayList<>();
    private DataManager dm = DataManager.getInstance();
    private CourseInfo mNoteCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);

//        mNotesViewModel.getAllCourses().observe(this, new Observer<List<CourseInfo>>() {
//            @Override
//            public void onChanged(List<CourseInfo> courses) {
////                DataManager.loadCoursesFromDatabase(courses);
//                mCourse = courses;
////                mNotesViewModel.getAllNotes().observe(NotesFragment.this, notes -> {
////                    Log.d(TAG, "onChanged: ");
////                    mNoteRecyclerAdapter.setNotes(mCourse,notes);
//////                    mNoteRecyclerAdapter.notifyDataSetChanged();
////
////                });
//
//
//            }
//        });

        mNotesViewModel.mData().observe(this, new Observer<List<NoteDao.noteJoincourse>>() {
            @Override
            public void onChanged(List<NoteDao.noteJoincourse> noteJoincourses) {
                mNoteRecyclerAdapter.setNotes(noteJoincourses);
                mNoteRecyclerAdapter.notifyDataSetChanged();


            }
        });


//        mNotesViewModel.getAllNotes().observe(this, notes -> {
//            Log.d(TAG, "onChanged: ");
////            mNoteRecyclerAdapter.setNotes(mCourse,notes);
//            mNoteRecyclerAdapter.notifyDataSetChanged();
//
//        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_notes, container, false);

        mRecyclerItems = mRoot.findViewById(R.id.list_items);
        mNotesLayoutManager = new LinearLayoutManager(getContext());
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(getContext());
        mRecyclerItems.setLayoutManager(mNotesLayoutManager);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);


        FloatingActionButton fab = mRoot.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NoteActivity.class));
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mNotesViewModel.delete(mNoteRecyclerAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(mRecyclerItems);


        return mRoot;
    }



    private void displayNotes() {
        mRecyclerItems.setLayoutManager(mNotesLayoutManager);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}