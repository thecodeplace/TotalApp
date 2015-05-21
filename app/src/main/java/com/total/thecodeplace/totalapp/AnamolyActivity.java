package com.total.thecodeplace.totalapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AnamolyActivity extends ActionBarActivity implements OnItemSelectedListener{


    Button buttonContinue;
    EditText txtSubject;
    EditText txtMessage;
    EditText txtPath;
    Spinner spinnerLocation;
    String spinnerLocationData;
    String subject;
    String reporter;
    EditText txtReporter;


    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamoly);

        buttonContinue = (Button) findViewById(R.id.buttonContinue);
        txtSubject = (EditText) findViewById(R.id.editTextSubject);
        txtReporter = (EditText) findViewById(R.id.editTextReporter);
        final File root   = Environment.getExternalStorageDirectory();
        final File dir = new File(root.getAbsolutePath() + "/PersonData");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        // Spinner element
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        // Spinner click listener
        spinnerLocation.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List <String> categories = new ArrayList <java.lang.String>();
        categories.add("MOKPO");
        categories.add("ULSAN");
        // Creating adapter for spinner
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <java.lang.String> (this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerLocation.setAdapter(dataAdapter);
        spinnerLocationData = String.valueOf(spinnerLocation.getSelectedItem());

        // Spinner element




        buttonContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnamolyActivity2.class);
                intent.putExtra("location", spinnerLocationData);
                intent.putExtra("site",subject);
                intent.putExtra("reporter",reporter);// getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

    }






    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        //item = item.concat(txtMessage.getText().toString());

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
