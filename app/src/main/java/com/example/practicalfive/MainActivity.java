package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private final TextProcessor textProcessor = new TextProcessor();
    private EditText municipalityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        municipalityInput = findViewById(R.id.municipality_input);
        municipalityInput.setOnEditorActionListener((view, actionId, event) -> {
            boolean enterKey = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            if (enterKey && (event.getAction() != KeyEvent.ACTION_DOWN || event.getRepeatCount() > 0)) {
                return true;
            }
            if (enterKey || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                openMunicipality();
                return true;
            }
            return false;
        });
    }

    private void openMunicipality() {
        String input = municipalityInput.getText().toString();
        String municipality = textProcessor.processMunicipality(input);
        municipalityInput.setError(null);
        if (municipality == null) {
            municipalityInput.setError(getString(input.trim().isEmpty()
                    ? R.string.input_required : R.string.invalid_municipality));
            municipalityInput.requestFocus();
            return;
        }

        InputMethodManager keyboard = getSystemService(InputMethodManager.class);
        if (keyboard != null) {
            keyboard.hideSoftInputFromWindow(municipalityInput.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, Municipality.class);
        intent.putExtra(Municipality.EXTRA_MUNICIPALITY, municipality);
        startActivity(intent);
    }
}
