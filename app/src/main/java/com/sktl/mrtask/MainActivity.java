package com.sktl.mrtask;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView mRecyclerView;
    private List<Process> processList;
    private MrTaskAdapter adapter;
    private String killingProcess = "";//удаляемый процесс


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    getNowAllProcess();//вызываем наш метод
                    initializeAdapter();//обновили список RecyclerView
                    mTextMessage.setText("Now run " + processList.size() + " apps");
                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    mTextMessage.setText("Now run " + processList.size() + " apps");
//
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    mTextMessage.setText("Now run " + processList.size() + " apps");
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        //для поддежки lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

    }


    private void initializeData() {
        processList = new ArrayList<>();
        processList = getNowAllProcess();

    }

    private void initializeAdapter() {
        adapter = new MrTaskAdapter(processList);
        mRecyclerView.setAdapter(adapter);
        final List<Process> processesT = getNowAllProcess();
        adapter = new MrTaskAdapter(processesT);
        //Прикрепим onItemClickListener
        adapter.setOnItemClickListener(new MrTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                itemView.setBackgroundColor(1);
                String name = processesT.get(position).getName();
                killApplication(name);
                getNowAllProcess();//вызываем наш метод
                initializeAdapter();//обновили список RecyclerView

                //выводим результат клика
                String resultMessage = name;
                for (Process p : processList) {
                    if (p.getName().equals(name)) {
                        resultMessage = "! Can't kill " + name;
                        break;
                    }
                }
                if (resultMessage.equals(name)) {
                    resultMessage = name + " was killed";
                }
                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_LONG).show();

                mTextMessage.setText("Now run " + processList.size() + " apps");
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * удаляет процесс по его имени пакета
     *
     * @param killPackage имя удаляемого пакета
     * @return имя удаляемого пакета
     */
    private String killApplication(String killPackage) {
        killingProcess = killPackage;
        if (killingProcess != "") {
            try {
                ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= 21) {


                UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                List<ActivityManager.AppTask> appTasks = am.getAppTasks();
                for (ActivityManager.AppTask appTask : appTasks) {
                    Log.d("eee", "appTask.getTaskInfo(): " + appTask.getTaskInfo().persistentId);

                    if (false) {
                        appTask.finishAndRemoveTask();
                    }
                }
            }


                am.killBackgroundProcesses(killPackage);


            } catch (Exception e) {
                Log.d("eee", "SKTL e: " + e);
            }
        } else {
            killingProcess = "Error 888: You did not specify the process that you are going to kill!";
        }
        return killingProcess;
    }


    /**
     * @return список запущенных приложений
     */

    private List<Process> getNowAllProcess() {
        processList = new ArrayList<>();
        Process processT;

        if (Build.VERSION.SDK_INT >= 22) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);


            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);

            // Sort the stats by the last time used
            if (stats != null) {
                for (UsageStats usageStats : stats) {
                    Log.d("eee", usageStats.getPackageName());

                    processT = new Process(usageStats.getPackageName());

                    processList.add(processT);

                }
            }
        } else {
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
            if (runningProcesses != null && runningProcesses.size() > 0) {
                for (ActivityManager.RunningAppProcessInfo elementRAppPInfo : runningProcesses) {

                    processT = new Process(String.valueOf(elementRAppPInfo.pid), elementRAppPInfo.processName);

                    processList.add(processT);
                }
            } else {
                Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
            }


        }
        return processList;
    }

}