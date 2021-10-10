package com.example.seniorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);
            finish();
        }
        EditText editTextEmail = findViewById(R.id.textEmailForgot);

        findViewById(R.id.textViewForgotPassword).setOnClickListener(v -> {
            findViewById(R.id.forgotPasswordSubmit).setVisibility(View.VISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.forgotPasswordSubmit).setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            ParseUser.requestPasswordResetInBackground(email, e -> {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "A password reset request was sent to your registered email.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        });


        findViewById(R.id.mainLogin).setOnClickListener(v -> {
            EditText LUsername = findViewById(R.id.loginUsername);
            EditText LPassword = findViewById(R.id.loginPassword);

            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Logging you in...");
            progressDialog.show();
            ParseUser.logInInBackground(LUsername.getText().toString(), LPassword.getText().toString(), (parseUser, e) -> {
                progressDialog.dismiss();
                if (parseUser != null) {
                    Toast.makeText(MainActivity.this, "Successful login. Welcome, " + parseUser.getUsername(), Toast.LENGTH_SHORT).show();
                    Log.i("loginLog", "Logged In.");
                    Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ParseUser.logOutInBackground();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        });


        findViewById(R.id.mainInventoryGuest).setOnClickListener(v -> {
            Intent intentInventory = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intentInventory);
            finish();
        });

        findViewById(R.id.mainRegister).setOnClickListener(v -> {
            Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intentRegister);
        });


    }

}

