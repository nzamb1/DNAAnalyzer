package com.dnaanalyzer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class MainActivity extends BaseActivity {

    JSONArray Disease    = new JSONArray();
    JSONArray Prbability = new JSONArray();
    JSONArray Icons      = new JSONArray();

    private RequestData requestData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Genetic Risk");
        }
        //toolbar.setSubtitle("Test Subtitle");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String uid = ((DnaApplication) this.getApplication()).getUid();

        requestData = new RequestData();
        requestData.execute(Constants.BACKEND_URL + "/basiccounters", uid, "secret");

        ListView disease_listView = findViewById(R.id.disease_listview);

        CustomAdapter customAdapter = new CustomAdapter();
        disease_listView.setAdapter(customAdapter);

        disease_listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DnaAnalyzer", "itemClick: position = " + position + ", id = " + id);
                TextView textView = view.findViewById(R.id.disease_textview);
                String text = textView.getText().toString();
                Log.d("DnaAnalyzer", "itemClick: text = " + text );

                Bundle b = new Bundle();
                b.putString("disease",text);

                Intent intent = new Intent(MainActivity.this, DiseaseDetailes.class);
                intent.putExtras(b);
                startActivity(intent);

            }
                                                }

        );
    }

    public class RequestData extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (result == null){
                Toast.makeText(MainActivity.this, R.string.NoConnection,
                        Toast.LENGTH_LONG).show();
            }
            else {
                Disease    = (JSONArray) result.get("Disease");
                Prbability = (JSONArray) result.get("Porbability");
                Icons      = (JSONArray) result.get("Icons");

                //Toast.makeText(MainActivity.this, result.toString(),
                //Toast.LENGTH_LONG).show();

                ListView disease_listView = findViewById(R.id.disease_listview);
                ((CustomAdapter) disease_listView.getAdapter()).notifyDataSetChanged();
            }

        }

        protected JSONObject doInBackground(String...params) {
            String urlString = params[0];
            String userName =  params[1];
            String password =  params[2];
            Log.i("DnaAnalyzer", "connecting to: " + urlString);
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;
            JSONObject jsonObject = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                //Uri fileUri = Uri.parse(filepath);

                Log.i("DnaAnalyzer", "Preparing data for sending");

                String data = URLEncoder.encode("userName", Constants.DEFAULT_CHARSET)
                        + "=" + URLEncoder.encode(userName, Constants.DEFAULT_CHARSET);


                urlConnection.connect();
                Log.i("DnaAnalyzer", "Ready to send request to server");

                OutputStreamWriter wr = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                String result = "";

                stream = urlConnection.getInputStream();
                BufferedReader httpreader = new BufferedReader(
                        new InputStreamReader(stream, Constants.DEFAULT_CHARSET), 8);


                JSONParser jsonParser = new JSONParser();

                jsonObject = (JSONObject) jsonParser.parse(
                        new InputStreamReader(stream, Constants.DEFAULT_CHARSET));

                Log.i("DnaAnalyzer", result);

                return jsonObject;

            } catch (Exception e) {
                Log.e("DnaAnalyzer",e.getMessage());

            }


            return jsonObject;
        }
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return Disease.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {


            view = getLayoutInflater().inflate(R.layout.diseaselistlayout,null);

            ImageView imageViewDisease = view.findViewById(R.id.disease_imageView);
            ImageView imageViewRisk = view.findViewById(R.id.imageGenRisk);
            TextView textview_disease = view.findViewById(R.id.disease_textview);

            textview_disease.setText(Disease.get(i).toString());
            imageViewDisease.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                            Base64.decode(
                                    Icons.get(i).toString(),
                                    Base64.DEFAULT), 0,
                            (Base64.decode(Icons.get(i).toString(),
                                    Base64.DEFAULT)).length));
            //
            if (Prbability.get(i).toString().equals("0"))
            {
                imageViewRisk.setImageDrawable(getResources().getDrawable(R.drawable.ic_performance_low));
            } else if (Prbability.get(i).toString().equals("1")) {
                imageViewRisk.setImageDrawable(getResources().getDrawable(R.drawable.ic_performance_medium));
            } else if (Prbability.get(i).toString().equals("2")){
                imageViewRisk.setImageDrawable(getResources().getDrawable(R.drawable.ic_performance_high));
            } else if (Prbability.get(i).toString().equals("3")){
                imageViewRisk.setImageDrawable(getResources().getDrawable(R.drawable.ic_performance_very_high));
            }


            return view;
        }
    }

}
