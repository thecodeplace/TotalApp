package com.total.thecodeplace.totalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class AnamolyActivity extends ActionBarActivity {

    Button buttonSend;
    EditText txtTo;
    EditText txtSubject;
    EditText txtMessage;

    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamoly);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        txtTo = (EditText) findViewById(R.id.editTextTo);
        txtSubject = (EditText) findViewById(R.id.editTextSubject);
        txtMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSend.setOnClickListener(new OnClickListener()

        {
            public void onClick(View v)

            {
                String to = txtTo.getText().toString();
                String subject = txtSubject.getText().toString();
                String message = txtMessage.getText().toString();
                if (to != null && to.length() == 0)

                {
                    Toast.makeText(getApplicationContext(),
                            "You forgot to enter the email ID",
                            Toast.LENGTH_SHORT).show();

                }

                else if (to != null && to.length() > 0 && !isEmailValid(to))

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
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email,
                            "Choose an Email client :"));

                }
            }
        });
    }

    boolean isEmailValid(CharSequence email)

    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
