package com.example.kerteszeti_weboldal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenu extends AppCompatActivity{
    private FirebaseAuth f_auth;
    private FirebaseFirestore f_store;
    TextView welcomeText;

    //Az áruknak
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private DocumentSnapshot lastVisible = null;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Intent profile = new Intent(this, Profile.class);
        Intent mainActivity = new Intent(this, MainActivity.class);
        welcomeText = findViewById(R.id.welcomeText);
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();

        //Az áruknak
        recyclerView = findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
        //

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

        loadItems();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && !isLastPage) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                        loadItems();
                    }
                }
            }
        });

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

    private void loadItems() {
        isLoading = true;
        Query query = f_store.collection("items").orderBy("name").limit(5);
        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    Log.d("FIRESTORE_DEBUG", "Documents found: " + snapshot.size());
                    lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        String name = doc.getString("name");
                        Long priceLong = doc.getLong("price");
                        String description = doc.getString("description");
                        Long quantityLong = doc.getLong("quantity");
                        /*Log.d("ITEM_DEBUG", "Name: " + name);
                        Log.d("ITEM_DEBUG", "Price: " + priceLong);
                        Log.d("ITEM_DEBUG", "Description: " + description);
                        Log.d("ITEM_DEBUG", "Quantity: " + quantityLong);*/

                        int price = priceLong != null ? priceLong.intValue() : 0;
                        int quantity = quantityLong != null ? quantityLong.intValue() : 0;

                        items.add(new Item(name, price, description, quantity));
                    }
                    adapter.setItems(items);

                    if (snapshot.size() < 5) {
                        isLastPage = true;
                    }
                } else {
                    isLastPage = true;
                }
            } else {
                Exception e = task.getException();
                Log.e("FIRESTORE_ERROR", "Hiba a lekérdezés során", e);
                Toast.makeText(MainMenu.this, "Nem lehet betölteni a termékeket!", Toast.LENGTH_SHORT).show();
            }
            isLoading = false;
        });
    }

    public void shop(View view) {
    }
}
