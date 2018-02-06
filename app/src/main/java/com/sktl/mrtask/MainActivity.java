package com.sktl.mrtask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private String allProcesses = "";//все запущенные процессы
    private String killingProcess = "";//удаляемый процесс

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    mTextMessage.setText(getNowAllProcess());//вызываем наш метод
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    mTextMessage.setText(killApplication("com.dropbox.android"));//хардкод для проверки
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
    public String getNowAllProcess() {
        allProcesses = "";
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null && runningProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo elementRAppPInfo : runningProcesses) {
                allProcesses = allProcesses +
                        elementRAppPInfo.processName + ",, " +
                        String.valueOf(elementRAppPInfo.pid) +
                        ", , , ";
            }
            mTextMessage.setText(allProcesses);
        } else {
            Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
            allProcesses = "";
        }
        return allProcesses;
    }


}
