package com.example.sonminhee.tmon_rtsearchwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by sonminhee on 2017. 7. 12..
 */

public class RTSearchWidgetDetail extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rtsearch_detail);


        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        Log.i("","TEST TEST RTSEARCH_DETAIL");
        Log.i("","받아온것 : " + title);

    }
}
