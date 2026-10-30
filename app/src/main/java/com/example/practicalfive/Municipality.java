package com.example.practicalfive;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Municipality extends AppCompatActivity {
    public static final String EXTRA_MUNICIPALITY = "com.example.practicalfive.MUNICIPALITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra(EXTRA_MUNICIPALITY);
        String municipality = new TextProcessor().processMunicipality(name);
        if (municipality == null) {
            Toast.makeText(this, R.string.invalid_extra, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.municipality_root), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        TextView nameView = findViewById(R.id.municipality_name);
        nameView.setText(municipality);
        nameView.setTextColor(getColor(R.color.color_text_dark));
        setTitle(municipality);
        findViewById(R.id.visit_site).setEnabled(false);
    }
}
