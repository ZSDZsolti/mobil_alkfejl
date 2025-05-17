package com.example.kerteszeti_weboldal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Profile extends AppCompatActivity{
    private FirebaseAuth f_auth;
    private FirebaseFirestore f_store;
    EditText lastName;
    EditText firstName;
    EditText userName;
    EditText email;
    EditText password;

    String uid;

    Intent mainActivity;
    Intent profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent mainMenu = new Intent(this, MainMenu.class);
        profile = new Intent(this, Profile.class);
        mainActivity = new Intent(this, MainActivity.class);
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        lastName = findViewById(R.id.lastName);
        firstName = findViewById(R.id.firstName);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        if (f_auth.getCurrentUser() != null) {
            uid = Objects.requireNonNull(f_auth.getCurrentUser()).getUid();

            f_store.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            lastName.setHint(documentSnapshot.getString(("lastName")));
                            firstName.setHint(documentSnapshot.getString(("firstName")));
                            userName.setHint(documentSnapshot.getString(("userName")));
                            email.setHint(f_auth.getCurrentUser().getEmail());
                        }
                    });
        }

        Spinner menu = findViewById(R.id.menu);

        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = parentView.getItemAtPosition(position).toString();


                switch (selected) {
                    case "Főoldal":
                        startActivity(mainMenu);
                        break;
                    case "Kijelentkezés":
                        f_auth.signOut();
                        startActivity(mainActivity);
                        finish();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


    }

    public void save(View view) {
        String lastNameText = lastName.getText().toString();
        String firstNameText = firstName.getText().toString();
        String userNameText = userName.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        AtomicBoolean emailOrPasswordChange = new AtomicBoolean(false);
        boolean elseChange = false;
        AtomicBoolean updateFail = new AtomicBoolean(false);

        if(uid != null){
            Map<String, Object> updates = new HashMap<>();
            if(!lastNameText.isEmpty()){
                updates.put("lastName", lastNameText);
                elseChange = true;
            }
            if(!firstNameText.isEmpty()){
                updates.put("firstName", firstNameText);
                elseChange = true;
            }
            if(!userNameText.isEmpty()){
                updates.put("userName", userNameText);
                elseChange = true;
            }
            if(!emailText.isEmpty()){
                emailOrPasswordChange.set(true);
            }
            if(!passwordText.isEmpty()){
                emailOrPasswordChange.set(true);
            }

            if(elseChange){
                f_store.collection("users").document(uid).update(updates)
                        .addOnSuccessListener(aVoid -> {
                            if(emailOrPasswordChange.get()){
                                if(!emailText.isEmpty()){
                                    Objects.requireNonNull(f_auth.getCurrentUser()).updateEmail(emailText);
                                }
                                if(!passwordText.isEmpty()){
                                    Objects.requireNonNull(f_auth.getCurrentUser()).updatePassword(passwordText);
                                }
                                f_auth.signOut();
                                startActivity(mainActivity);
                                finish();
                                return;
                            }
                            Toast.makeText(Profile.this, "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                            startActivity(profile);
                        })
                        .addOnFailureListener(e -> {
                            updateFail.set(true);
                            Toast.makeText(Profile.this, "Sikertelen módosítás: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
            if(emailOrPasswordChange.get() && !updateFail.get()){
                if(!emailText.isEmpty()){
                    Objects.requireNonNull(f_auth.getCurrentUser()).updateEmail(emailText);
                }
                if(!passwordText.isEmpty()){
                    Objects.requireNonNull(f_auth.getCurrentUser()).updatePassword(passwordText);
                }
                f_auth.signOut();
                startActivity(mainActivity);
                finish();
            }
        }
    }
}
