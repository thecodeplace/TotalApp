package com.total.thecodeplace.totalapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Button buttonTakePic;
    Button 
    EditText txtSubject;
    EditText txtMessage;
    EditText txtPath;
    Spinner spinnerLocation;
    Spinner spinnerGoldenRules;
    String spinnerLocationData;
    EditText txtReporter;
    EditText txtImmediateAction;
    final int RQS_LOADIMAGE = 0;
    Uri imageUri = null;
    Uri photoPath = null;
    final int TAKE_PICTURE = 1;
    int takepicture = 0;
    int attachpicture=0;
    int i = 0;

    String[] GoldenRules = {"High-Risk Situations", "Traffic", "Body Mechanics and Tools",
            "Protective Equipment", "Work Permits", "Lifting Operations", "Powered Systems","Confined Spaces","Excavation Work","Work at Height"
    ,"Change Management","Simultaneous Operations or Co-Activities"};


    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamoly);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonAttach = (Button) findViewById(R.id.buttonAttach);
        buttonTakePic = (Button) findViewById(R.id.buttonTakePicture);
        txtSubject = (EditText) findViewById(R.id.editTextSubject);
        txtMessage = (EditText) findViewById(R.id.editTextMessage);
        txtReporter = (EditText) findViewById(R.id.editTextReporter);
        txtImmediateAction = (EditText) findViewById(R.id.editImmediateAction);
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

        // Spinner element
        spinnerGoldenRules = (Spinner) findViewById(R.id.spinnerGoldenRules);
        spinnerGoldenRules.setAdapter(new MyCustomAdapter(AnamolyActivity.this, R.layout.row, GoldenRules));
        // Spinner click listener
        spinnerGoldenRules.setOnItemSelectedListener(this);

        buttonSend.setOnClickListener(new OnClickListener()

        {
            public void onClick(View v)

            {
                String subject = txtSubject.getText().toString();
                String message = txtMessage.getText().toString();

                if (subject != null && subject.length() == 0)
                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the site",
                            Toast.LENGTH_SHORT).show();
                }

                else if (message != null && message.length() == 0)

                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the message",
                            Toast.LENGTH_SHORT).show();
                }

                else if (subject != null && message != null)
                {

                    String columnString =   "\"Reporter\",\"Location\",\"Golden Rule\",\"Site\",\"Description\",\"Immediate Action\"";
                    String reporter = txtReporter.getText().toString();
                    spinnerLocationData = String.valueOf(spinnerLocation.getSelectedItem());
                    String goldenRuleData = String.valueOf(spinnerGoldenRules.getSelectedItem());
                    String immediateAction = txtImmediateAction.getText().toString();

                    String dataString   =   "\"" + reporter +"\",\"" + spinnerLocationData + "\",\"" + goldenRuleData + "\",\"" + subject + "\",\"" + message + "\",\""+ immediateAction + "\"";
                    String combinedString = columnString + "\n" + dataString;

                    File file   = null;

                    if (root.canWrite()) {
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

                    File bitmapFile = new File(dir+"/Pic.png");
                    Uri myUri = Uri.fromFile(bitmapFile);

                    Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                    String toList[] = { "vanipriya@thecodeplace.com" };
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
                   /* String ccToList[] = {"daniel.guerra-paez@total.com"};
                    sendIntent.putExtra(Intent.EXTRA_CC, ccToList);*/
                    ArrayList<Uri> uris = new ArrayList<Uri>();


                    uris.add(i,u1);
                    i++;

                    if(takepicture==1) {
                        uris.add(i, myUri);
                        i++;
                    }

                    if(attachpicture==1)
                    {
                        uris.add(i,imageUri);
                    }


                    sendIntent.putExtra(Intent.EXTRA_STREAM, uris);
                    sendIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(sendIntent,
                            "Choose an Email client :"));

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

        buttonTakePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file1 = new File(dir, "Pic.png");
                photoPath = Uri.fromFile(file1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
                // start camera activity
                startActivityForResult(intent,TAKE_PICTURE);
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
                    attachpicture = 1;
                    break;

            }

        }

        if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && data != null){
            // get bundle
            takepicture = 1;
            Bundle extras = data.getExtras();
            Log.v("Photo Path",photoPath.toString());
            photoPath = data.getData();
            txtPath.setText(photoPath.toString());

            // get bitmap
            /*bitMap = (Bitmap) extras.get("data");
            ivThumbnailPhoto.setImageBitmap(bitMap);*/

        }
    }

    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.weekofday);
            label.setText(GoldenRules[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.icon);

            switch(GoldenRules[position]){
                case "High-Risk Situations" : icon.setImageResource(R.drawable.one);
                                              break;
                case "Traffic"   : icon.setImageResource(R.drawable.two);
                                   break;
                case "Body Mechanics and Tools" : icon.setImageResource(R.drawable.three);
                    break;
                case "Protective Equipment" :icon.setImageResource(R.drawable.four);
                    break;
                case "Work Permits" :icon.setImageResource(R.drawable.five);
                    break;
                case "Lifting Operations" :icon.setImageResource(R.drawable.six);
                    break;
                case "Powered Systems" :icon.setImageResource(R.drawable.seven);
                    break;
                case "Confined Spaces" :icon.setImageResource(R.drawable.eight);
                    break;
                case "Excavation Work" :    icon.setImageResource(R.drawable.nine);
                    break;
                case "Work at Height" : icon.setImageResource(R.drawable.ten);
                    break;
                case "Change Management" :icon.setImageResource(R.drawable.eleven);
                    break;
                case "Simultaneous Operations or Co-Activities" :icon.setImageResource(R.drawable.twelve);
                    break;

            }


            return row;
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
