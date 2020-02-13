package com.systec.noteapp.uploader;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.systec.noteapp.database.DataManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_UrI = "com.systec.noteapp.extras.dATA_Uri";
    private static int NUMBER_OF_THREADS = 2;
    private ExecutorService uploadExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private NoteUploader mNoteUploader;

    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mNoteUploader = new NoteUploader(this);

        uploadExecutor.execute(() ->{
            String stringUri = jobParameters.getExtras().getString(EXTRA_DATA_UrI);
            Uri dataUri = Uri.parse(stringUri);
            NoteUploader.doUpload(DataManager.getInstance().getData);


            if (mNoteUploader.isCanceled()){
                jobFinished(jobParameters, false);

            }
        });


//        @SuppressLint("StaticFieldLeak") AsyncTask<JobParameters,Void,Void> task = new AsyncTask<JobParameters, Void, Void>() {
//            @Override
//            protected Void doInBackground(JobParameters... backgroundParameters) {
//
//
//
//
//                return null;
//            }
//        };
//        task.execute(jobParameters);

//        new tasks(this).execute(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mNoteUploader.canceled();
        return true;
    }

    private static class tasks extends AsyncTask<JobParameters,Void,Void>{
        @SuppressLint("StaticFieldLeak")
        private Context mContext;

        tasks(Context context){
            mContext = context;
        }
        @Override
        protected Void doInBackground(JobParameters... backgroundParameters) {
            NoteUploader noteUploader = new NoteUploader(mContext);




            return null;
        }
    }


}
