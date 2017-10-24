package com.example.sonminhee.tmon_rtsearchwidget;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by sonminhee on 2017. 7. 4..
 */

public class RTSearchWidgetConfigure extends AppCompatActivity implements View.OnClickListener {
    private int mAppWidgetId;
    private SeekBar SeekTransparent;
    private int transparent = 50; //default
    private LinearLayout bg_widget;
    private String bg_value;

    private static final String ACTION_WIDGET_CONFIGURE = "CONFIGURE_CLICK";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rtsearch_widget_configure);

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.imageview);

        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.setBackground(wallpaperDrawable);

        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        SeekTransparent = (SeekBar) findViewById(R.id.seek_transparent);
        SeekTransparent.setProgress(transparent);

        SeekTransparent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateBackground(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("","TEST TEST TOUCH");
                //선택막대를 터치하고 드래그를 시작할 때 실행되는 메소드
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //seekbar 움직이다가 멈췄을 때
                TextView tv = (TextView) findViewById(R.id.transparentResult);
                tv.setText(tv.getText());
                Log.i("","TEST TEST STOP TOUCH");

            }
        });
    }

    public void updateBackground(int value) {
        TextView tv = (TextView) findViewById(R.id.transparentResult);
        tv.setText(String.valueOf(value));

        bg_widget = (LinearLayout) findViewById(R.id.bg_widget);
        bg_value = "#" + percent_to_hex(value) + "ffffff";

        bg_widget.setBackgroundColor(Color.parseColor(bg_value));
    }

    /*percent 값을 hex 값으로(투명도)*/
    public String percent_to_hex(int value) {
        float per = (float) (value / 100.0);
        int rounded = (int) Math.round(per * 255);
        String hex = Integer.toHexString(rounded).toUpperCase();

        if (hex.length() == 1)
            hex = "0" + hex;
        return hex;
    }

    @Override
    public void onClick(View v) {

        SharedPreferences sharedPreferences = getSharedPreferences("VALUE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (v.getId() == R.id.btn_confirm) {
            Log.i("","TEST TEST CONFIRM");

            //sharedPreferences에 저장
            editor.putString("selectedValue", bg_value);
            editor.commit();


            setIntent();
            Log.i("","TEST TEST setINTENT");
        }else {
            editor.putString("selectedValue", "#80ffffff");
            editor.commit();

            setIntent();
        }

        //configure 종료 알림
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    private void setIntent(){
        Intent intent = new Intent(RTSearchWidgetConfigure.this, RTSearchWidget.class);
        intent.setAction(ACTION_WIDGET_CONFIGURE);
        RTSearchWidgetConfigure.this.sendBroadcast(intent);
    }


}
