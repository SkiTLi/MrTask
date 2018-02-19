package com.sktl.mrtask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USER-PC on 18.02.2018.
 */

public class CardViewActivity extends Activity {

    ImageView processPhoto;
    TextView processName;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_view);
        processName = (TextView)findViewById(R.id.process_name);
        button = (Button)findViewById(R.id.process_button);
        processPhoto = (ImageView)findViewById(R.id.process_photo);

        processName.setText("Emma Wilson");
        processPhoto.setImageResource(R.drawable.work_performance256);
    }


}
