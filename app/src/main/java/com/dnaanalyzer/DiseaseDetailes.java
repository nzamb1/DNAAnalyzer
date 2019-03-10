package com.dnaanalyzer;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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


public class DiseaseDetailes extends BaseActivity {
    JSONArray Description    = new JSONArray();
    JSONArray Rsid           = new JSONArray();
    JSONArray Mutation       = new JSONArray();

    private DiseaseDetailes.RequestData requestData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_disease_detailes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String diseasename = b.getString("disease");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(diseasename);
        }

        String uid = ((DnaApplication) this.getApplication()).getUid();
        String backendurl = ((DnaApplication) this.getApplication()).getbackendUrl();

        requestData = new RequestData();
        requestData.execute(backendurl + "/getdiseasedetails", uid, diseasename);

        ListView details_listView = (ListView) findViewById(R.id.details_listview);

        DiseaseDetailes.DiseaseDetailsAdapter customAdapter = new DiseaseDetailsAdapter();
        details_listView.setAdapter(customAdapter);


//        TextView textView = (TextView) findViewById(R.id.diseasedetails_textView);
//        textView.setText(diseasename);
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
                Toast.makeText(DiseaseDetailes.this, "Error! No connection to server.",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Description    = (JSONArray) result.get("Description");
                Rsid           = (JSONArray) result.get("Rsid");
                Mutation       = (JSONArray) result.get("Mutation");

                //Toast.makeText(MainActivity.this, result.toString(),
                //Toast.LENGTH_LONG).show();

                ListView details_listView = (ListView) findViewById(R.id.details_listview);;
                ((DiseaseDetailes.DiseaseDetailsAdapter) details_listView.getAdapter()).notifyDataSetChanged();
            }

        }

        protected JSONObject doInBackground(String...params) {
            String urlString   = params[0];
            String userName    = params[1];
            String diseasename = params[2];
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

                Log.i("DnaAnalyzer", "Preparing data for sending");

                String data = URLEncoder.encode("userName", "UTF-8")
                        + "=" + URLEncoder.encode(userName, "UTF-8");

                data += "&" + URLEncoder.encode("diseasename", "UTF-8") + "="
                        + URLEncoder.encode(diseasename, "UTF-8");



                urlConnection.connect();
                Log.i("DnaAnalyzer", "Ready to send request to server");

                OutputStreamWriter wr = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                String result = "";

                stream = urlConnection.getInputStream();
                BufferedReader httpreader = new BufferedReader(
                        new InputStreamReader(stream, "UTF-8"), 8);


                JSONParser jsonParser = new JSONParser();

                jsonObject = (JSONObject) jsonParser.parse(
                        new InputStreamReader(stream, "UTF-8"));

                Log.i("DnaAnalyzer", result);

                return jsonObject;

            } catch (Exception e) {
                Log.e("DnaAnalyzer",e.getMessage());

            }


            return jsonObject;
        }
    }

    class DiseaseDetailsAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return Description.size();
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


            view = getLayoutInflater().inflate(R.layout.diseaseproperties,null);

            TextView textview_rsid = (TextView)view.findViewById(R.id.rsIDtextView);
            TextView textview_description = (TextView)view.findViewById(R.id.descriptiontextView);

            textview_rsid.setText(Rsid.get(i).toString());
            textview_description.setText(Description.get(i).toString());

            return view;
        }
    }
}
