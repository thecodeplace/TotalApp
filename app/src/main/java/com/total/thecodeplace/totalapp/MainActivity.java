package com.total.thecodeplace.totalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button anamolyButton = (Button) findViewById(R.id.anamolyButton);
        anamolyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), AnamolyActivity.class);
                startActivity(i);
            }
        });


        Button initiativeButton = (Button) findViewById(R.id.initiativeButton);
        initiativeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), InitiativeActivity.class);
                startActivity(i);
            }
        });
    }



}
