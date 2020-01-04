package com.dnaanalyzer;

import android.os.AsyncTask;
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
import java.nio.charset.StandardCharsets;


public class TraitDetailes extends BaseActivity {
    JSONArray Description    = new JSONArray();
    JSONArray Rsid           = new JSONArray();
    JSONArray Mutation       = new JSONArray();

    private TraitDetailes.RequestData requestData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trait_detailes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        String traitname = b.getString("traits");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(traitname);
        }

        String uid = ((DnaApplication) this.getApplication()).getUid();

        requestData = new RequestData();
        requestData.execute(Constants.BACKEND_URL + "/gettraitdetails", uid, traitname);

        ListView details_listView = findViewById(R.id.traits_listview);

        TraitDetailes.TraitDetailsAdapter customAdapter = new TraitDetailsAdapter();
        details_listView.setAdapter(customAdapter);


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
                Toast.makeText(TraitDetailes.this, "Error! No connection to server.",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Description    = (JSONArray) result.get("Description");
                Rsid           = (JSONArray) result.get("Rsid");
                Mutation       = (JSONArray) result.get("Mutation");

                //Toast.makeText(MainActivity.this, result.toString(),
                //Toast.LENGTH_LONG).show();

                ListView details_listView = findViewById(R.id.traits_listview);
                ((TraitDetailes.TraitDetailsAdapter) details_listView.getAdapter()).notifyDataSetChanged();
            }

        }

        protected JSONObject doInBackground(String...params) {
            String urlString   = params[0];
            String userName    = params[1];
            String traitname = params[2];
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

                data += "&" + URLEncoder.encode("traitname", "UTF-8") + "="
                        + URLEncoder.encode(traitname, "UTF-8");


                Log.i("DnaAnalyzer", "Connecting to server...");
                urlConnection.connect();
                Log.i("DnaAnalyzer", "Ready to send request to server");

                OutputStreamWriter wr = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                String result = "";

                stream = urlConnection.getInputStream();
                BufferedReader httpreader = new BufferedReader(
                        new InputStreamReader(stream, "UTF-8"));


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

    class TraitDetailsAdapter extends BaseAdapter {

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


            view = getLayoutInflater().inflate(R.layout.traitproperties,null);

            TextView textview_rsid = view.findViewById(R.id.TrsIDtextView);
            TextView textview_description = view.findViewById(R.id.TdescriptiontextView);
            ImageView chromoimageView = view.findViewById(R.id.TchromoimageView);

            textview_rsid.setText(Rsid.get(i).toString());
            textview_description.setText(Description.get(i).toString());

            if (Mutation.get(i).toString().equals("AG")) {
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_ag));
            } else if (Mutation.get(i).toString().equals("AA")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_aa));
            } else if (Mutation.get(i).toString().equals("AC")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_ac));
            } else if (Mutation.get(i).toString().equals("AT")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_at));
            } else if (Mutation.get(i).toString().equals("CC")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_cc));
            } else if (Mutation.get(i).toString().equals("CG")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_cg));
            } else if (Mutation.get(i).toString().equals("GC")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_gc));
            } else if(Mutation.get(i).toString().equals("GG")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_gg));
            } else if (Mutation.get(i).toString().equals("TA")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_ta));
            }else if (Mutation.get(i).toString().equals("TC")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_tc));
            } else if (Mutation.get(i).toString().equals("TG")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_tg));
            } else if (Mutation.get(i).toString().equals("TT")){
                chromoimageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_chromo_tt));
            }

            return view;
        }
    }
}
