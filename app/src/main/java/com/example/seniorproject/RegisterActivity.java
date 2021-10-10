package com.example.seniorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseUser;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ArrayList<EditText> EditTextList = new ArrayList<>();
        ArrayList<TextView> TextErrorList = new ArrayList<>();

        EditTextList.add(findViewById(R.id.registerUsername));
        EditTextList.add(findViewById(R.id.registerEmail));
        EditTextList.add(findViewById(R.id.registerPassword));
        EditTextList.add(findViewById(R.id.registerPasswordAgain));

        TextErrorList.add(findViewById(R.id.registerErrorUsername));
        TextErrorList.add(findViewById(R.id.registerErrorEmail));
        TextErrorList.add(findViewById(R.id.registerErrorPassword));
        TextErrorList.add(findViewById(R.id.registerErrorPasswordAgain));


        findViewById(R.id.registerRegister).setOnClickListener(v -> {
            boolean isReady = true;
            for (int i = 0; i < EditTextList.size(); i++) {
                if (EditTextList.get(i).getText().toString().isEmpty()) {
                    isReady = false;
                    TextErrorList.get(i).setVisibility(View.VISIBLE);
                } else
                    TextErrorList.get(i).setVisibility(View.INVISIBLE);
            }


            if (!EditTextList.get(2).getText().toString().equals(EditTextList.get(3).getText().toString())) {
                TextErrorList.get(3).setVisibility(View.VISIBLE);
                isReady = false;
            }

            if (isReady) {
                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(EditTextList.get(0).getText().toString());
                parseUser.setEmail(EditTextList.get(1).getText().toString());
                parseUser.setPassword(EditTextList.get(2).getText().toString());
                parseUser.signUpInBackground(e -> {
                    if (e == null) {
                        Log.i("registerLog", "Registered.");
                        Toast.makeText(RegisterActivity.this, "Registration Successful. Please verify your email to log in.", Toast.LENGTH_LONG).show();
                        RegisterActivity.this.finish();
                    } else {
                        TextView Error = findViewById(R.id.registerError);
                        Error.setText(e.getMessage());
                        Error.setVisibility(View.VISIBLE);
                        Log.i("registerErrorLog", e.getMessage());
                        e.printStackTrace();
                    }

                });
            }
        });

    }
}