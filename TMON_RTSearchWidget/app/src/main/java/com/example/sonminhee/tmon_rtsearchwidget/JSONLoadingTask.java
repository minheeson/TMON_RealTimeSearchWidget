package com.example.sonminhee.tmon_rtsearchwidget;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sonminhee on 2017. 7. 11..
 */

public class JSONLoadingTask extends AsyncTask<Void, String, String> {

    private static final String TAG = "JSONLoadingTask";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        android.util.Log.v(TAG, "TEST TEST onPreExecute");
    }

    @Override
    protected String doInBackground(Void... params) {

        android.util.Log.v(TAG, "TEST TEST doInBackground");
        return getJsonText();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        android.util.Log.v(TAG, "TEST TEST onPostExecute : " + s);
    }


    /**
     * JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText() {

        StringBuffer sb = new StringBuffer();

        try {
            String jsonPage = getStringFromUrl("https://apis.daum.net/search/book?apikey=" + "5f8f392ba538d5e34ba0c2ced0a1a113" + "&q=다음카카오&output=json");

            JSONObject json = new JSONObject(jsonPage);

            JSONArray jArr = json.getJSONArray("data");

            for (int i = 0; i < 10; i++) {
                json = jArr.getJSONObject(i);
                String title = json.getString("title");
                sb.append(title + "\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    /**
     * 주어진 URL의 문서의 내용을 문자열로 반환
     */
    public String getStringFromUrl(String pUrl) {
        BufferedReader buf = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer(); //읽어온 데이터 저장하는 버퍼

        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            buf = new BufferedReader(new InputStreamReader(contentStream, "UTF-8"));

            String line = null;

            while ((line = buf.readLine()) != null) {
                Log.d("line : ", line);
                page.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buf.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return page.toString();
    }

}
