package com.sktl.mrtask

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private var mTextMessage: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var processList: MutableList<Process>? = null
    private var adapter: MrTaskAdapter? = null
    private var killingProcess: String? = ""//удаляемый процесс


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                mTextMessage!!.setText(R.string.title_home)
                nowAllProcess//вызываем наш метод
                initializeAdapter()//обновили список RecyclerView
                mTextMessage!!.text = "Now run " + processList!!.size + " apps"
                return@OnNavigationItemSelectedListener true
            }
        }//                case R.id.navigation_dashboard:
        //                    mTextMessage.setText(R.string.title_dashboard);
        //                    mTextMessage.setText("Now run " + processList.size() + " apps");
        //
        //                    return true;
        //                case R.id.navigation_notifications:
        //                    mTextMessage.setText(R.string.title_notifications);
        //                    mTextMessage.setText("Now run " + processList.size() + " apps");
        //                    return true;
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mTextMessage = findViewById(R.id.message) as TextView
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView

        val llm = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = llm
        mRecyclerView!!.setHasFixedSize(true)

        initializeData()
        initializeAdapter()

        //для поддежки lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

    }


    private fun initializeData() {
        processList = ArrayList()
        processList = nowAllProcess

    }

    private fun initializeAdapter() {
        adapter = MrTaskAdapter(processList!!)
        mRecyclerView!!.adapter = adapter
        val processesT = nowAllProcess
        adapter = MrTaskAdapter(processesT)
        //Прикрепим onItemClickListener
        adapter!!.setOnItemClickListener(object : MrTaskAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                itemView.setBackgroundColor(1)
                val name = processesT[position].name
                killApplication(name)
                nowAllProcess//вызываем наш метод
                initializeAdapter()//обновили список RecyclerView

                //выводим результат клика
                var resultMessage = name
                for (p in processList!!) {
                    if (p.name == name) {
                        resultMessage = "! Can't kill " + name!!
                        break
                    }
                }
                if (resultMessage == name) {
                    resultMessage = name!! + " was killed"
                }
                Toast.makeText(applicationContext, resultMessage, Toast.LENGTH_LONG).show()

                mTextMessage!!.text = "Now run " + processList!!.size + " apps"
            }
        })
        adapter!!.notifyDataSetChanged()
    }

    /**
     * удаляет процесс по его имени пакета
     *
     * @param killPackage имя удаляемого пакета
     * @return имя удаляемого пакета
     */
    private fun killApplication(killPackage: String?): String? {
        killingProcess = killPackage
        if (killingProcess !== "") {
            try {
                val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                if (Build.VERSION.SDK_INT >= 21) {


                    val mUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                    val appTasks = am.appTasks
                    for (appTask in appTasks) {
                        Log.d("eee", "appTask.getTaskInfo(): " + appTask.taskInfo.persistentId)

                        if (false) {
                            appTask.finishAndRemoveTask()
                        }
                    }
                }


                am.killBackgroundProcesses(killPackage)


            } catch (e: Exception) {
                Log.d("eee", "SKTL e: " + e)
            }

        } else {
            killingProcess = "Error 888: You did not specify the process that you are going to kill!"
        }
        return killingProcess
    }


    /**
     * @return список запущенных приложений
     */

    private // We get usage stats for the last 10 seconds
            // Sort the stats by the last time used
    val nowAllProcess: MutableList<Process>
        get() {
            processList = ArrayList()
            var processT: Process

            if (Build.VERSION.SDK_INT >= 22) {
                val mUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager


                val time = System.currentTimeMillis()
                val stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time)
                if (stats != null) {
                    for (usageStats in stats) {
                        Log.d("eee", usageStats.packageName)

                        processT = Process(usageStats.packageName)

                        processList!!.add(processT)

                    }
                }
            } else {
                val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningProcesses = manager.runningAppProcesses
                if (runningProcesses != null && runningProcesses.size > 0) {
                    for (elementRAppPInfo in runningProcesses) {

                        processT = Process(elementRAppPInfo.pid.toString(), elementRAppPInfo.processName)

                        processList!!.add(processT)
                    }
                } else {
                    Toast.makeText(applicationContext, "No application is running", Toast.LENGTH_LONG).show()
                }


            }
            return processList as ArrayList<Process>
        }

}