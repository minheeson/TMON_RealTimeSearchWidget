package com.example.sonminhee.tmon_rtsearchwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class RTSearchWidget extends AppWidgetProvider {

    private static final String ACTION_WIDGET_CONFIGURE = "CONFIGURE_CLICK";
    private static final String ACTION_WIDGET_DETAIL = "DETAIL_CLICK";
    private static final String ACTION_WIDGET_REFRESH = "REFRESH_CLICK";
    private static final String TAG = "RTSearchWidget";

    String str_value;
    String data[];

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (action.equals(ACTION_WIDGET_CONFIGURE)) {
            Log.i(TAG, "TEST TEST CONFIGURE");
            SharedPreferences sharedPreferences = context.getSharedPreferences("VALUE", MODE_PRIVATE);
            str_value = sharedPreferences.getString("selectedValue", "fail");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            this.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, RTSearchWidget.class)));
            return;
        } else if (action.startsWith(ACTION_WIDGET_DETAIL)) {
            String title = action.substring(action.indexOf("s"));
            Intent detailIntent = new Intent(context, RTSearchWidgetDetail.class);
            detailIntent.putExtra("title", "샤오미");
            context.startActivity(detailIntent);


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            this.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, RTSearchWidget.class)));
            return;


        } else if (action.equals(ACTION_WIDGET_REFRESH)) {
            Log.i(TAG, "TEST TEST REFRESH");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        Log.i(TAG, "TEST TEST WIDGET START");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.rtsearch_widget);

        remoteViews.setInt(R.id.change_bg, "setBackgroundColor", Color.parseColor(str_value));

        Intent confIntent = new Intent(context, RTSearchWidgetConfigure.class);
        PendingIntent confPendingIntent = PendingIntent.getActivity(context, 0, confIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_to_conf, confPendingIntent);

        Intent webIntent = new Intent();
        webIntent.setAction(ACTION_WIDGET_DETAIL + "_" + 3);
        Log.i("", "GETACTION : " + webIntent.getAction());
        PendingIntent webPendingIntent = PendingIntent.getBroadcast(context, 0, webIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        for (int i = 0; i < 10; i++) {
            int resId = context.getResources().getIdentifier("s" + i, "id", context.getPackageName());
            Log.i("", "INDEX : " + i);
            remoteViews.setOnClickPendingIntent(resId, webPendingIntent);
        }

        jsonLoading(context, remoteViews);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    private void jsonLoading(Context context, RemoteViews remoteViews) {
        JSONLoadingTask jsonLoadingTask = new JSONLoadingTask();

        try {
            String s = jsonLoadingTask.execute().get();
            splitSearch(s);
            updateList(context, remoteViews, data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void splitSearch(String str) {
        data = str.split("\\r?\\n");
        for (int i = 0; i < data.length; i++) {
            System.out.println("TEST TEST SPLIT DATA : " + i + " : " + data[i]);
        }
    }

    private void updateList(Context context, RemoteViews remoteViews, String[] arr) {
        String pkg = context.getPackageName();

        for (int i = 0; i < arr.length; i++) {
            int resId = context.getResources().getIdentifier("s" + i, "id", pkg);
            remoteViews.setTextViewText(resId, data[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //host(바탕화면)에 위젯이 처음 추가될 경우 호출, 데이터 베이스를 열거나 다른 추가 작업을 해줘야하는 경우
    }

    @Override
    public void onDisabled(Context context) {
        //바탕화면에서 동일한 위젯이 여러개 설치되어 있다고 하면, 그 중 제일 마지막 위젯이 삭제된 후 호출

    }
}
