package com.example.kerteszeti_weboldal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private FirebaseAuth f_auth;
    private FirebaseFirestore f_store;

    EditText lastName;
    EditText firstName;
    EditText userName;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
    }

    public void register(View view) {
        Intent registration = new Intent(this, MainActivity.class);
        String lastNameString = lastName.getText().toString();
        String firstNameString = firstName.getText().toString();
        String userNameString = userName.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        f_auth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> user = new HashMap<>();
                    user.put("lastName", lastNameString);
                    user.put("firstName", firstNameString);
                    user.put("userName", userNameString);

                    String uid = Objects.requireNonNull(f_auth.getCurrentUser()).getUid();
                    f_store.collection("users").document(uid).set(user);

                    startActivity(registration);
                }
                else{
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}