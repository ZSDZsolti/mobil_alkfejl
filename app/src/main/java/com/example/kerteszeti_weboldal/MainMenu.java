package com.example.kerteszeti_weboldal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainMenu extends AppCompatActivity{
    private FirebaseAuth f_auth;
    private FirebaseFirestore f_store;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Intent profile = new Intent(this, Profile.class);
        Intent mainActivity = new Intent(this, MainActivity.class);
        welcomeText = findViewById(R.id.welcomeText);
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();

        if (f_auth.getCurrentUser() != null) {
           String uid = Objects.requireNonNull(f_auth.getCurrentUser()).getUid();

            f_store.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            welcomeText.setText(documentSnapshot.getString("userName"));

                        } else {
                            String text = "Üdvözlünk!";
                            welcomeText.setText(text);
                        }
                    });
        }

        Spinner menu = findViewById(R.id.menu);

        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = parentView.getItemAtPosition(position).toString();


                switch (selected) {
                    case "Profilom":
                        startActivity(profile);
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
}
