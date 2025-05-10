package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView back = findViewById(R.id.ic_arrow_back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
        });
        ImageView mapImage = findViewById(R.id.google_maps); // ID correct de l'image
        mapImage.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MapsActivity.class);
            startActivity(intent);
        });



        // Tu peux ajouter des setOnClickListener ici pour chaque image si besoin
    }
}
