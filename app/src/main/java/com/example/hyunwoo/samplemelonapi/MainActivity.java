package com.example.hyunwoo.samplemelonapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Initialize
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);




        // API 연결 Task 실행!
        new MelonTask().execute(1, 50);


    }



    public static final String MELON_URL = "http://apis.skplanetx.com/melon/charts/realtime?version=1&page=%d&count=%d";

    public class MelonTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            // 접속.. Melon API
            int page = params[0];
            int count = params[1];
            String urlText = String.format(MELON_URL, page, count);

            try {
                URL url = new URL(urlText);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("appKey", "3e0923ca-353e-35e2-9265-eb3227a53ef9");
                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {        // == CODE 200 (성공)
                    // 연결이 성공했다!
                    // responseString을 받자.
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line=br.readLine()) != null) {
                        sb.append(line).append("\n\r");
                    }
                    return sb.toString();
                    // Pasing! >> onPostExecute(String s)       // 리턴된 responseString이 들어감
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            // MelonTask가 execute된 이후에 실행되는 callback method

            // Pasing을 여기서!!
            Gson gson = new Gson();
            MelonResult result = gson.fromJson(responseString, MelonResult.class);

            for (Song song : result.melon.songs.song) {
                mAdapter.add(song.songName);
            }

        }
    }




}








