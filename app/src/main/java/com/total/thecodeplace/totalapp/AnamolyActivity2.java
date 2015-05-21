package com.total.thecodeplace.totalapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


public class AnamolyActivity2 extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    Button buttonSend;
    Button buttonAttach;
    Button buttonTakePic;
    Button buttonContinue;
    EditText txtSubject;
    EditText txtMessage;
    EditText txtPath;
    Spinner spinnerLocation;
    Spinner spinnerGoldenRules;
    String spinnerLocationData;
    String subject;
    String location;
    String reporter;
    EditText txtReporter;
    EditText txtImmediateAction;
    final int RQS_LOADIMAGE = 0;
    Uri imageUri = null;
    Uri photoPath = null;
    final int TAKE_PICTURE = 1;
    int takepicture = 0;
    int attachpicture = 0;
    int i = 0;
    ArrayList<Uri> uris = new ArrayList<Uri>();
    final File root   = Environment.getExternalStorageDirectory();
    final File dir = new File(root.getAbsolutePath() + "/PersonData");

    String[] GoldenRules = {"High-Risk Situations", "Traffic", "Body Mechanics and Tools",
            "Protective Equipment", "Work Permits", "Lifting Operations", "Powered Systems","Confined Spaces","Excavation Work","Work at Height"
            ,"Change Management","Simultaneous Operations or Co-Activities"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamoly_activity2);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonAttach = (Button) findViewById(R.id.buttonAttach);
        buttonTakePic = (Button) findViewById(R.id.buttonTakePicture);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);
        txtMessage = (EditText) findViewById(R.id.editTextMessage);
        txtImmediateAction = (EditText) findViewById(R.id.editImmediateAction);
        subject = getIntent().getExtras().getString("site");
        location = getIntent().getExtras().getString("location");
        reporter = getIntent().getExtras().getString("reporter");

        if(!dir.exists()) {
            dir.mkdirs();
        }


        // Spinner element
        spinnerGoldenRules = (Spinner) findViewById(R.id.spinnerGoldenRules);
        spinnerGoldenRules.setAdapter(new MyCustomAdapter(AnamolyActivity2.this, R.layout.row, GoldenRules));
        // Spinner click listener
        spinnerGoldenRules.setOnItemSelectedListener(this);

        buttonSend.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v)

            {


                String message = txtMessage.getText().toString();

                if (message != null && message.length() == 0)

                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the message",
                            Toast.LENGTH_SHORT).show();
                } else {

                    String columnString = "\"Reporter\",\"Location\",\"Golden Rule\",\"Site\",\"Description\",\"Immediate Action\"";

                    //spinnerLocationData = String.valueOf(spinnerLocation.getSelectedItem());
                    String goldenRuleData = String.valueOf(spinnerGoldenRules.getSelectedItem());
                    String immediateAction = txtImmediateAction.getText().toString();

                    String dataString = "\"" + reporter + "\",\"" + location + "\",\"" + goldenRuleData + "\",\"" + subject + "\",\"" + message + "\",\"" + immediateAction + "\"";
                    String combinedString = columnString + "\n" + dataString;

                    File file = null;

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
                    Uri u1 = null;
                    u1 = Uri.fromFile(file);



                    Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                    String toList[] = {"vanipriya@thecodeplace.com"};
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, toList);
                   /* String ccToList[] = {"daniel.guerra-paez@total.com"};
                    sendIntent.putExtra(Intent.EXTRA_CC, ccToList);*/

                    uris.add(i, u1);
                    i++;

                    File bitmapFile = new File(dir + "/Pic.png");
                    Uri myUri = Uri.fromFile(bitmapFile);
                    uris.add(i,myUri);
                    i++;
                    /*if(imageUri!=null) {
                        uris.add(i, imageUri);
                        i++;
                    }*/

                    sendIntent.putExtra(Intent.EXTRA_STREAM, uris);
                    sendIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(sendIntent,
                            "Choose an Email client :"));

                }
            }
        });

        buttonAttach.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v)

            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_LOADIMAGE);


            }
        });

        buttonTakePic.setOnClickListener(new View.OnClickListener() {
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
                    uris.add(i,imageUri);
                    i++;
                    break;

            }

        }

        if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && data != null){
            // get bundle
            takepicture = 1;
            Bundle extras = data.getExtras();
            /*Log.v("Photo Path", photoPath.toString());
            photoPath = data.getData();
            txtPath.setText(photoPath.toString());*/


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
