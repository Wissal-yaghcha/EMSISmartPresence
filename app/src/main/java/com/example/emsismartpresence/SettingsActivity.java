package com.example.emsismartpresence;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);  // Assurez-vous que ce layout contient bien le Switch

        themeSwitch = findViewById(R.id.theme_switch);

        // Vérifier l'état actuel du mode sombre et l'appliquer
        boolean isDarkMode = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("dark_mode", false);  // false = mode clair par défaut
        // Appliquer le mode sombre/clair en fonction de la préférence enregistrée
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Initialiser l'état du Switch selon la préférence
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("settings", MODE_PRIVATE).edit()
                    .putBoolean("dark_mode", isChecked)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            Toast.makeText(SettingsActivity.this, "Mode changé", Toast.LENGTH_SHORT).show();
        });

    }
}
