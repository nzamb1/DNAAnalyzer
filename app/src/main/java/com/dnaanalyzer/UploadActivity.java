package com.dnaanalyzer;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.dnaanalyzer.util.AppIOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class UploadActivity extends BaseActivity {

    private View mProgressView;
    private TestAsync mTestAsync = null;


    public void checkfileuploaded(){
        //Check if file is already uploaded and send user to MainNavigationActivity
        SharedPreferences settings = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
        if (settings.contains("fileuploaded")){
            if (settings.getBoolean("fileuploaded", true)){
                Intent intent = new Intent(UploadActivity.this, MainNavigation.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        checkfileuploaded();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressView = findViewById(R.id.progressBar);

        checkfileuploaded();

        }


    public void buttonObClick(View v){

        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri selectedfileuri = data.getData(); //The uri with the location of the file
            InputStream readeris = null;

            String contenttype = null;
            StringBuilder sb = new StringBuilder();

            ContentResolver cR = getApplicationContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            contenttype = mime.getExtensionFromMimeType(cR.getType(selectedfileuri));

            if ("csv".equals(contenttype) || "zip".equals(contenttype)) {
                try {

                    readeris = getApplicationContext().getContentResolver().openInputStream(selectedfileuri);
                    byte[] filedatabytes = AppIOUtils.toByteArray(readeris);

                    String uid = ((DnaApplication) this.getApplication()).getUid();

                    Log.i("DnaAnalyzer", "Sending data to async thread: ");
                    Log.d("DnaAnalyzer", "UID: " + uid);
                    mTestAsync = new TestAsync();
                    mTestAsync.execute(Constants.BACKEND_URL + "/develfile", uid, "secret", contenttype, filedatabytes);

                } catch (Exception e) {
                    Log.e("DnaAnalyzer", e.getMessage());

                }
            } else {
                Toast.makeText(UploadActivity.this, "Only CSV and ZIP files are supported",
                Toast.LENGTH_SHORT).show();

            }
        }

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
    public class TestAsync extends AsyncTask<Object, Integer, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);
        }

        protected String doInBackground(Object...params) {

            String urlString = (String) params[0];
            String userName =  (String) params[1];
            String password =  (String) params[2];
            String filetype =  (String) params[3];
            byte[] rawdata =  (byte[]) params[4];
            String filedata = Base64.encodeToString(rawdata, Base64.DEFAULT);

            Log.i("DnaAnalyzer", "connecting to: " + urlString);
            URL url = null;
            InputStream stream = null;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                Log.i("DnaAnalyzer", "Preparing data for sending");

                String data = URLEncoder.encode("userName", "UTF-8")
                        + "=" + URLEncoder.encode(userName, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");

                data += "&" + URLEncoder.encode("filetype", "UTF-8") + "="
                        + URLEncoder.encode(filetype, "UTF-8");

                data += "&" + URLEncoder.encode("rawdata", "UTF-8") + "="
                        + URLEncoder.encode(filedata, "UTF-8");

                Log.d("DnaAnalyzer", "Connecting to backend...");
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                String result = "";

                stream = urlConnection.getInputStream();
                BufferedReader httpreader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);

                result = httpreader.readLine();


                if (urlConnection.getResponseCode() == 200) {
                    return "ok";
                } else {
                    return "Upload failed, Return code is not 200";
                }



            } catch (Exception e) {
                Log.e("DnaAnalyzer","1 - Failed to upload data: " + e.getMessage());
                Log.e("DnaAnalyzer","exception", e);

            }
            Log.e("DnaAnalyzer", "2 - Failed to upload data");
            return "UploadFailed";
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);



            if ("ok".equals(result)){
                Toast.makeText(UploadActivity.this, "File uploaded and processed successfully.",
                        Toast.LENGTH_SHORT).show();

                SharedPreferences settings = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean( "fileuploaded", true );
                editor.commit();

                Intent intent = new Intent(UploadActivity.this, MainNavigation.class);
                startActivity(intent);

            } else {
                Toast.makeText(UploadActivity.this, "File upload error: " + result,
                        Toast.LENGTH_SHORT).show();

            }


        }
    }
}









