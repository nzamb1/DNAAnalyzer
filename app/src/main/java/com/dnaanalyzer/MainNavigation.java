package com.dnaanalyzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainNavigation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button HealthInfo = (Button) findViewById(R.id.health_info_button);
        HealthInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigation.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button SearchRsid = (Button) findViewById(R.id.searchrsidbutton);
        SearchRsid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigation.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
