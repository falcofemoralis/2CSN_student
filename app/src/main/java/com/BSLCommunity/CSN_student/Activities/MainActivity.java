package com.BSLCommunity.CSN_student.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.BSLCommunity.CSN_student.Activities.Schedule.ScheduleActivity;
import com.BSLCommunity.CSN_student.Managers.JSONHelper;
import com.BSLCommunity.CSN_student.Objects.Groups;
import com.BSLCommunity.CSN_student.Objects.LocalData;
import com.BSLCommunity.CSN_student.Objects.Subjects;
import com.BSLCommunity.CSN_student.Objects.Teachers;
import com.BSLCommunity.CSN_student.Objects.Timer;
import com.BSLCommunity.CSN_student.Objects.User;
import com.BSLCommunity.CSN_student.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import static com.BSLCommunity.CSN_student.Objects.Settings.encryptedSharedPreferences;
import static com.BSLCommunity.CSN_student.Objects.Settings.setSettingsFile;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static String MAIN_URL = "http://a0459938.xsph.ru/";
    public static String FILE_NAME = "data_disc";
    public static String GROUP_FILE_NAME = "groups";

    Timer timer = new Timer(); //таймер
    TextView Time, TimeUntil; //переменные таймера

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_main_ll_main);
        for (int i=5;i<linearLayout.getChildCount();i+=2){
            TableRow tableRow = (TableRow) linearLayout.getChildAt(i);
            tableRow.getChildAt(0).setOnTouchListener(this);
            tableRow.getChildAt(2).setOnTouchListener(this);
        }

        Time = (TextView) findViewById(R.id.activity_main_tv_timerCounter);
        TimeUntil = (TextView) findViewById(R.id.activity_main_tv_timer_text);

        setSettingsFile(this);

        Boolean is_registered = encryptedSharedPreferences.getBoolean(SettingsActivity.KEY_IS_REGISTERED, false);
        if (!is_registered) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else {
            TextView courseTextView = (TextView) findViewById(R.id.activity_main_tv_course);
            TextView groupTextView = (TextView) findViewById(R.id.activity_main_tv_group);

            courseTextView.setText(String.valueOf(User.getInstance().course) + " Course");
            groupTextView.setText(User.getInstance().nameGroup + " Group");
        }

        // Скачиваем все необходимые апдейт листы для проверки актуальности данных и проверяем данные
        LocalData.downloadUpdateList(getApplicationContext(), LocalData.updateListGroups, LocalData.TypeData.groups, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Groups.init(getApplicationContext(), User.getInstance().course, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        LocalData.checkUpdate(getApplicationContext(), LocalData.TypeData.groups);
                        return null;
                    }
                });
                return null;
            }
        });

        LocalData.downloadUpdateList(getApplicationContext(), LocalData.updateListTeachers, LocalData.TypeData.teachers, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Teachers.init(getApplicationContext(), new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        LocalData.checkUpdate(getApplicationContext(), LocalData.TypeData.teachers);
                        return null;
                    }
                });
                return null;
            }
        });

        Subjects.init(getApplicationContext(), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            transitionDrawable.startTransition(150);
            view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.btn_pressed));
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.activity_main_bt_subjects:
                    intent = new Intent(this, SubjectListActivity.class);
                    break;
                case R.id.activity_main_bt_rating:
                    intent = new Intent(this, RatingActivity.class);
                    break;
                case R.id.activity_main_bt_lessonsShedule:
                    intent = new Intent(this, ScheduleActivity.class).putExtra("typeSchedule", "Groups");
                    break;
                case R.id.activity_main_bt_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    break;
                case R.id.activity_main_bt_teachersSchedule:
                    intent = new Intent(this, ScheduleActivity.class).putExtra("typeSchedule", "Teachers");
                    break;
            }

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            startActivity(intent, options.toBundle());

            transitionDrawable.reverseTransition(150);
            view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.btn_unpressed));
        }
        return true;
    }

    public void OnClick(View view) {
      /*  Intent intent = null;
        switch (view.getId()) {
            case R.id.activity_main_bt_subjects:
                intent = new Intent(this, SubjectList.class);
                break;
            case R.id.activity_main_bt_rating:
                intent = new Intent(this, Rating.class);
                break;
            case R.id.activity_main_bt_lessonsShedule:
                intent = new Intent(this, Schedule.class).putExtra("typeSchedule", "Groups");
                break;
            case R.id.activity_main_bt_settings:
                intent = new Intent(this, Settings.class);
                break;
            case R.id.activity_main_bt_teachersSchedule:
                intent = new Intent(this, Schedule.class).putExtra("typeSchedule", "Teachers");
                break;
        }
        startActivity(intent);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.checkTimer(TimeUntil, Time, getResources());
    }

    @Override
    protected void onPause() {
        timer.resetTimer();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void checkData() {
        boolean offline_data = encryptedSharedPreferences.getBoolean(SettingsActivity.KEY_OFFLINE_DATA, false);
        if (checkConnection()) {
            if (offline_data)
                showDialog();
            // else
            //   loadStatusFromServer();
        } //else
        //  loadStatusFromDevice();
    }

    protected void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        String PATH = this.getFileStreamPath(MainActivity.FILE_NAME).toString();
        File file = new File(PATH);
        if (file.exists()) {
            long last = file.lastModified();
            Date date = new Date(last);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
            alertDialog.setTitle(getResources().getString(R.string.localdata_is_found) + " " + format.format(date));
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RatingActivity>>() {
            }.getType();

            String test = JSONHelper.read(MainActivity.this, "data_disc.json");
            // discs = gson.fromJson(test, listType);

            // for (int i = 0; i < discs.size(); ++i)
            // SubjectInfo.getStatus(encryptedSharedPreferences.getString(Settings.KEY_NICKNAME, ""), i, this);
            return;
        }

        alertDialog.setMessage(R.string.get_data_dialog);

        alertDialog.setPositiveButton(R.string.device, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  loadStatusFromDevice();
            }
        });

        alertDialog.setNegativeButton(R.string.server, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //loadStatusFromServer();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    protected boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

   /* protected void loadStatusFromServer() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Rating>>() {
        }.getType();
        discs = gson.fromJson(JSONHelper.read(Main.this, Main.FILE_NAME), listType);

        for (int i = 0; i < discs.size(); ++i)
            SubjectInfo.getStatus(encryptedSharedPreferences.getString(Settings.KEY_NICKNAME, ""), i, this);
    }

    protected void loadStatusFromDevice() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Rating>>() {
        }.getType();
        discs = gson.fromJson(JSONHelper.read(Main.this, Main.FILE_NAME), listType);

        for (int i = 0; i < discs.size(); ++i) {
            Rating temp = discs.get(i);
            SubjectInfo.updateRating(encryptedSharedPreferences.getString(Settings.KEY_NICKNAME, ""), temp.getName(), gson.toJson(temp.getComplete()), temp.getIDZ(), this);
        }
    }*/
}
