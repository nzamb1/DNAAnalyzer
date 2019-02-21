package com.dnaanalyzer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private RequestData requestData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText = (EditText)findViewById(R.id.editText2);
        editText.setText("Google is your friend.", TextView.BufferType.NORMAL);
        requestData = new RequestData();
        requestData.execute("http://192.168.0.191:5000/basiccounters", "nzamb1", "secret");

    }
    public class RequestData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            EditText editText = (EditText)findViewById(R.id.editText2);
            editText.setText(result, TextView.BufferType.NORMAL);
        }

        protected String doInBackground(String...params) {
            String urlString = params[0];
            String userName =  params[1];
            String password =  params[2];
            Log.i("DnaAnalyzer", "connecting to: " + urlString);
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                //Uri fileUri = Uri.parse(filepath);

                Log.i("DnaAnalyzer", "Preparing data for sending");

                String data = URLEncoder.encode("userName", "UTF-8")
                        + "=" + URLEncoder.encode(userName, "UTF-8");


                urlConnection.connect();
                Log.i("DnaAnalyzer", "Ready to send request to server");

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                String result = "";

                stream = urlConnection.getInputStream();
                BufferedReader httpreader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);


                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(
                        new InputStreamReader(stream, "UTF-8"));

                result = jsonObject.toString();

                Log.i("DnaAnalyzer", result);

                return result;

            } catch (Exception e) {
                Log.e("DnaAnalyzer",e.getMessage());

            }


            return "Done";
        }
    }
}
