package com.dnaanalyzer;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    protected final void onCreate(Bundle savedInstanceState, int layoutId)
    {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                //newGame();
                FirebaseAuth.getInstance().signOut();

                CharSequence text = "Successfully Logout!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);

                return true;
            case R.id.options:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
