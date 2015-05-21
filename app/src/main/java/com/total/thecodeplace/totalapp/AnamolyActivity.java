package com.total.thecodeplace.totalapp;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AnamolyActivity extends ActionBarActivity implements OnItemSelectedListener{

    Button buttonSend;
    Button buttonAttach;
    EditText txtTo;
    EditText txtSubject;
    EditText txtMessage;
    Spinner spinnerLocation;
    Spinner spinnerGoldenRules;
    String spinnerLocationData,attachmentFile;
    EditText txtReporter;
    EditText txtImmediateAction;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    final int RQS_LOADIMAGE = 0;
    final int RQS_SENDEMAIL = 1;

    Uri imageUri = null;
    int columnIndex;

    TextView textImagePath;


    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamoly);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonAttach = (Button) findViewById(R.id.buttonAttach);
        txtSubject = (EditText) findViewById(R.id.editTextSubject);
        txtMessage = (EditText) findViewById(R.id.editTextMessage);
        txtReporter = (EditText) findViewById(R.id.editTextReporter);
        txtImmediateAction = (EditText) findViewById(R.id.editImmediateAction);

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

        // Spinner element
        spinnerGoldenRules = (Spinner) findViewById(R.id.spinnerGoldenRules);
        // Spinner click listener
        spinnerGoldenRules.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List <String> goldenRules = new ArrayList <java.lang.String>();
        goldenRules.add("High-Risk Situations");
        goldenRules.add("Traffic");
        goldenRules.add("Body Mechanics and Tools");
        goldenRules.add("Protective Equipment");
        goldenRules.add("Work Permits");
        goldenRules.add("Lifting Operations");
        goldenRules.add("Powered Systems");
        goldenRules.add("Confined Spaces");
        goldenRules.add("Excavation Work");
        goldenRules.add("Work at Height");
        goldenRules.add("Change Management");
        goldenRules.add("Simultaneous Operations or Co-Activities");

        // Creating adapter for spinner
        ArrayAdapter <String> dataAdapterRules = new ArrayAdapter <java.lang.String> (this, android.R.layout.simple_spinner_item, goldenRules);
        // Drop down layout style - list view with radio button
        dataAdapterRules.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerGoldenRules.setAdapter(dataAdapterRules);


        buttonSend.setOnClickListener(new OnClickListener()

        {
            public void onClick(View v)

            {
                String to = txtTo.getText().toString();
                String subject = txtSubject.getText().toString();
                String message = txtMessage.getText().toString();


                if (to != null && to.length() > 0 && !isEmailValid(to))

                {
                    Toast.makeText(getApplicationContext(),
                            "Entered email ID is not Valid", Toast.LENGTH_SHORT).show();
                } else if (subject != null && subject.length() == 0)

                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the subject",
                            Toast.LENGTH_SHORT).show();
                }

                else if (message != null && message.length() == 0)

                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the message",
                            Toast.LENGTH_SHORT).show();
                }

                else if (to != null && subject != null && message != null)
                {



                    String columnString =   "\"Reporter\",\"Location\",\"Golden Rule\",\"Site\",\"Description\",\"Immediate Action\"";


                    String reporter = txtReporter.getText().toString();
                    spinnerLocationData = String.valueOf(spinnerLocation.getSelectedItem());
                    String goldenRuleData = String.valueOf(spinnerGoldenRules.getSelectedItem());
                    String immediateAction = txtImmediateAction.getText().toString();

                    String dataString   =   "\"" + reporter +"\",\"" + spinnerLocationData + "\",\"" + goldenRuleData + "\",\"" + to + "\",\"" + message + "\",\""+ immediateAction + "\"";
                    String combinedString = columnString + "\n" + dataString;

                    File file   = null;
                    File root   = Environment.getExternalStorageDirectory();
                    if (root.canWrite()) {
                        File dir = new File(root.getAbsolutePath() + "/PersonData");
                        dir.mkdirs();
                        file = new File(dir, "Data.csv");
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.write(combinedString.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    Uri u1  =   null;
                    u1  =   Uri.fromFile(file);

                    Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                    String toList[] = { "sivakumar_somu@hotmail.com" };
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
                    String ccToList[] = {"daniel.guerra-paez@total.com"};
                    sendIntent.putExtra(Intent.EXTRA_CC, ccToList);
                    ArrayList<Uri> uris = new ArrayList<Uri>();

                    uris.add(0,u1);
                    if(imageUri != null) {
                        uris.add(1,imageUri);
                    }

                        sendIntent.putExtra(Intent.EXTRA_STREAM, uris);

                    sendIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(sendIntent,
                            "Choose an Email client :"));

                    //email.putExtra(Intent.EXTRA_TEXT, spinnerLocationData);


                }
            }
        });

        buttonAttach.setOnClickListener(new OnClickListener()

        {
            public void onClick(View v)

            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_LOADIMAGE);


            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_LOADIMAGE:
                    imageUri = data.getData();
                    textImagePath.setText(imageUri.toString());
                    break;

            }

        }
    }




    boolean isEmailValid(CharSequence email)

    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        item = item.concat(txtMessage.getText().toString());

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
