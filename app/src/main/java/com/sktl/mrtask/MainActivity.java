package com.sktl.mrtask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
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
    MrTaskAdapter adapter;

    private String allProcesses = "";//все запущенные процессы
    private String killingProcess = "";//удаляемый процесс

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    mTextMessage.setText(getNowAllProcess());//вызываем наш метод
                    getNowAllProcess();//вызываем наш метод

                    initializeAdapter();//обновили список RecyclerView
                    mTextMessage.setText("Now run " + processList.size() + " apps");

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    mTextMessage.setText("Now run " + processList.size() + " apps");

                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    mTextMessage.setText("Now run " + processList.size() + " apps");
                    return true;
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


    }

    //это лишнее т.к. initializeData() == getNowAllProcess()
    private void initializeData() {
        processList = new ArrayList<>();
        processList = getNowAllProcess();
    }

    private void initializeAdapter() {
        adapter = new MrTaskAdapter(processList);
        mRecyclerView.setAdapter(adapter);


        //
        final List<Process> processesT = getNowAllProcess();
        adapter = new MrTaskAdapter(processesT);
        //Прикрепим onItemClickListener
        adapter.setOnItemClickListener(new MrTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

              itemView.setBackgroundColor(1);

                killApplication(processesT.get(position).getName());

//                String name = processesT.get(position).name;
//                Toast.makeText(Main.this, name, Toast.LENGTH_SHORT).show();
            }
        });
//


        adapter.notifyDataSetChanged();


    }


    /**
     * удаляет процесс по его имени пакета
     *
     * @param killPackage имя удаляемого пакета
     * @return имя удаляемого пакета
     */
    public String killApplication(String killPackage) {
        killingProcess = killPackage;
        if (killingProcess != "") {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            am.killBackgroundProcesses(killPackage);
        } else {
            killingProcess = "Error 888: You did not specify the process that you are going to kill!";
        }
        return killingProcess;
    }


    /**
     * (сейчас в виде строки)
     *
     * @return все запущенные приложения
     */
//    public String getNowAllProcess() {
//        allProcesses = "";
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
//        if (runningProcesses != null && runningProcesses.size() > 0) {
//            for (ActivityManager.RunningAppProcessInfo elementRAppPInfo : runningProcesses) {
//                allProcesses = allProcesses +
//                        elementRAppPInfo.processName + ",, " +
//                        String.valueOf(elementRAppPInfo.pid) +
//                        ", , , ";
//            }
//            mTextMessage.setText(allProcesses);
//        } else {
//            Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
//            allProcesses = "";
//        }
//        return allProcesses;
//    }
    public List<Process> getNowAllProcess() {
        processList = new ArrayList<>();
        Process processT;

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo elementRAppPInfo : runningProcesses) {

//                String[] lines = (elementRAppPInfo.processName).split("\\.");//берем только название приложения
//                processT = new Process(String.valueOf(elementRAppPInfo.pid), lines[lines.length-1]);

                processT = new Process(String.valueOf(elementRAppPInfo.pid), elementRAppPInfo.processName);

                processList.add(processT);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
        }
        return processList;
    }


}
