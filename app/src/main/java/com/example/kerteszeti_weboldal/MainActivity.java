package com.example.kerteszeti_weboldal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
    }

    public void login(View view){
        String userNameString = userName.getText().toString();
    }

    public void register(View view) {
        Intent registration = new Intent(this, Register.class);
        startActivity(registration);
    }
}
