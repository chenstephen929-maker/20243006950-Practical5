package com.example.practicalfive;

import android.os.Bundle;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
        showMunicipality(municipality);
        findViewById(R.id.visit_site).setEnabled(false);
    }

    private void showMunicipality(String municipality) {
        int summaryIndex;
        int imageResource;
        int colourResource;
        // The prose specification requires Chongqing before Tianjin in the array.
        switch (municipality) {
            case "Beijing":
                summaryIndex = 0;
                imageResource = R.drawable.beijing;
                colourResource = R.color.municipality_beijing;
                break;
            case "Shanghai":
                summaryIndex = 1;
                imageResource = R.drawable.shanghai;
                colourResource = R.color.municipality_shanghai;
                break;
            case "Chongqing":
                summaryIndex = 2;
                imageResource = R.drawable.chongqing;
                colourResource = R.color.municipality_chongqing;
                break;
            case "Tianjin":
                summaryIndex = 3;
                imageResource = R.drawable.tianjin;
                colourResource = R.color.municipality_tianjin;
                break;
            default:
                finish();
                return;
        }

        int municipalityColour = getColor(colourResource);
        int foreground = getColor("Chongqing".equals(municipality)
                ? R.color.color_text_dark : R.color.color_text_light);
        TextView name = findViewById(R.id.municipality_name);
        TextView information = findViewById(R.id.municipality_info);
        ImageView image = findViewById(R.id.municipality_image);
        ScrollView informationScroll = findViewById(R.id.municipality_info_scroll);
        Button visitSite = findViewById(R.id.visit_site);

        name.setText(municipality);
        name.setBackgroundColor(municipalityColour);
        name.setTextColor(foreground);
        information.setText(getResources().getStringArray(R.array.municipalities_array)[summaryIndex]);
        information.setTextColor(municipalityColour);
        // Bright orange requires a dark panel to keep the mandated information colour readable.
        informationScroll.setBackgroundColor(getColor("Chongqing".equals(municipality)
                ? R.color.color_text_dark : R.color.color_default_background));
        image.setImageResource(imageResource);
        image.setContentDescription(getString(R.string.municipality_image_description, municipality));
        image.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        visitSite.setBackgroundTintList(ColorStateList.valueOf(municipalityColour));
        visitSite.setTextColor(foreground);
    }

}
