package com.dnaanalyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dnaanalyzer.util.LoginUtils;


public abstract class BaseActivity extends AppCompatActivity {

    public static final String STORAGE_NAME = "DnaAnalyzer";


    protected final void onCreate(Bundle savedInstanceState, int layoutId)
    {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        assert myToolbar != null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_upload:
                SharedPreferences settings = getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean( "fileuploaded", false );
                editor.commit();

                intent = new Intent(BaseActivity.this, UploadActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                LoginUtils.logout(this);
                return true;

            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
