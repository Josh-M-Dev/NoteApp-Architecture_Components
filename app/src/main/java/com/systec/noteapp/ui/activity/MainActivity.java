package com.systec.noteapp.ui.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.systec.noteapp.BuildConfig;
import com.systec.noteapp.backup.NoteBackup;
import com.systec.noteapp.backup.NoteBackupService;
import com.systec.noteapp.uploader.NoteUploaderJobService;
import com.systec.noteapp.R;
import com.systec.noteapp.ui.notes.NotesViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NOTE_UPLOADER_JOB_ID = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService backgroundExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final String TAG = "MainActivity";
    private String mUserName;
    private String mEmailAddress;
    private TextView mTextUserName;
    private TextView mTextEmailAddress;
    private SharedPreferences mPref;
    private NotesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_notes, R.id.nav_courses, R.id.nav_slideshow,
                R.id.nav_share)
                .setDrawerLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        mNavigationView.setNavigationItemSelectedListener(this);

        /*
        Used for simulating NoteBackup And NoteUpload
        uncomment to use
         */

//        mViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
//        mViewModel.mData().observe(this, new Observer<List<NoteDao.noteJoincourse>>() {
//            @Override
//            public void onChanged(List<NoteDao.noteJoincourse> noteJoincourses) {
//                DataManager.getInstance().getData = noteJoincourses;
//            }
//        });

        enableStrinctModPolicy();

    }

    private void enableStrinctModPolicy() {
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.action_backup) {
            backupNotes();
        } else if (id == R.id.action_upload) {
            shcedulenoteUpload();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shcedulenoteUpload() {
        PersistableBundle extras = new PersistableBundle();
        extras.putString(NoteUploaderJobService.EXTRA_DATA_UrI, "");

        ComponentName componentName = new ComponentName(getPackageName(), NoteUploaderJobService.class.getName());
        JobInfo jobInfo =
                new JobInfo.Builder(NOTE_UPLOADER_JOB_ID, componentName)
//                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_CELLULAR)
//                        .setOverrideDeadline(500)
                        .setExtras(extras)
                        .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);


    }


    private void backupNotes() {
        Intent intent = new Intent(this, NoteBackupService.class);
        intent.putExtra(NoteBackupService.EXTRA_COURSE_ID, NoteBackup.ALL_COURSES);
        startService(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        switch (menuItem.getItemId()) {

            case R.id.nav_courses:
                navController.navigate(R.id.nav_courses);
                break;
            case R.id.nav_notes:
                navController.navigate(R.id.nav_notes);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share to " + PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social", ""), Toast.LENGTH_SHORT).show();


                break;

            case R.id.nav_send:
//                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

        }


        mDrawer.closeDrawers();


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavHeader();
//            openDrawer();
    }

    private void openDrawer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        }, 1500);
    }

    private void selectNavigationMenuItem(int id) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(id);
        Menu menu = navigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mTextUserName = headerView.findViewById(R.id.text_user_name);
        mTextEmailAddress = headerView.findViewById(R.id.text_email_address);

        Log.d(TAG, "call to thread " + Thread.currentThread().getId());

//        backgroundExecutor.execute(() ->{
//            Log.d(TAG, "call to thread " + Thread.currentThread().getId());
//
//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//            mUserName = pref.getString("user_display_name", "Your Name");
//            mEmailAddress = pref.getString("user_email_address", "youremail@yourhost.com");
//            updateTextViews(mUserName,mEmailAddress);
//
//        });


        String userName = mPref.getString("user_display_name", "Your Name");
        String emailAddress = mPref.getString("user_email_address", "youremail@yourhost.com");

        mTextUserName.setText(userName);
        mTextEmailAddress.setText(emailAddress);
    }

    private void updateTextViews(String userName, String emailAddress) {
        mTextUserName.setText(mUserName);
        mTextEmailAddress.setText(mEmailAddress);
    }


}
