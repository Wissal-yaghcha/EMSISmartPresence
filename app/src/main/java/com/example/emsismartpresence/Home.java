package com.example.emsismartpresence;

import android.content.Intent;
import android.os.Bundle;
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

        ImageView mapImage = findViewById(R.id.ic_maps);
        mapImage.setOnClickListener(v -> startActivity(new Intent(Home.this, MapsActivity.class)));

        ImageView assistantImage = findViewById(R.id.ic_assistant);
        assistantImage.setOnClickListener(v -> startActivity(new Intent(Home.this, Assistant_virtuel.class)));

        ImageView absencesImage = findViewById(R.id.ic_absences);
        absencesImage.setOnClickListener(v -> startActivity(new Intent(Home.this, AbsencesActivity.class)));

        ImageView docsImage = findViewById(R.id.ic_documents);
        docsImage.setOnClickListener(v -> startActivity(new Intent(Home.this, DocumentsActivity.class)));

        ImageView emploiBtn = findViewById(R.id.ic_emploi);
        emploiBtn.setOnClickListener(v -> startActivity(new Intent(Home.this, EmploiDuTempsActivity.class)));

        ImageView rattrapageBtn = findViewById(R.id.ic_rattrapage);
        rattrapageBtn.setOnClickListener(v -> startActivity(new Intent(Home.this, RattrapagesActivity.class)));
    }
}