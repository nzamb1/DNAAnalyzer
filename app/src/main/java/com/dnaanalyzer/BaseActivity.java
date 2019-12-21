package com.dnaanalyzer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dnaanalyzer.util.LoginUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete();
                    Log.d("DnaAnalyzer", "User account deleted.");
                    Toast.makeText(BaseActivity.this, R.string.AccDeleted, Toast.LENGTH_SHORT).show();
                    LoginUtils.logout(BaseActivity.this, false);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
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

            case R.id.delete_account:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.DeleteAccountYesNo).setPositiveButton(R.string.Yes, dialogClickListener)
                        .setNegativeButton(R.string.No, dialogClickListener).show();
                return true;

            case R.id.logout:
                LoginUtils.logout(this, true);
                return true;

            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
