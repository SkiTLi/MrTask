package com.sktl.mrtask

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by USER-PC on 18.02.2018.
 */

class CardViewActivity : Activity() {

    internal lateinit var processPhoto: ImageView
    internal lateinit var processName: TextView
    internal lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.item_view)
        processName = findViewById<TextView>(R.id.process_name)
        button = findViewById<Button>(R.id.process_button)
        processPhoto = findViewById<ImageView>(R.id.process_photo)

        processName.text = "Emma Wilson"
        processPhoto.setImageResource(R.drawable.work_performance256)
    }


}
