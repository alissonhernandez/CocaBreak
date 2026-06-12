package com.example.cocabreak;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfiguracionInicialActivity extends AppCompatActivity {

    LinearLayout cardCoca1, cardCoca2, cardCoca3;
    LinearLayout cardAgua1, cardAgua2, cardAgua3;
    Button btnContinuar;
    int cocaSeleccionada = 0;
    int aguaSeleccionada = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracion_inicial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cardCoca1 = findViewById(R.id.cardCoca1);
        cardCoca2 = findViewById(R.id.cardCoca2);
        cardCoca3 = findViewById(R.id.cardCoca3);
        cardAgua1 = findViewById(R.id.cardAgua1);
        cardAgua2 = findViewById(R.id.cardAgua2);
        cardAgua3 = findViewById(R.id.cardAgua3);
        btnContinuar = findViewById(R.id.btnContinuar);

        cardCoca1.setOnClickListener(v -> {
            cocaSeleccionada = 2;
            resetCoca();
            cardCoca1.setBackgroundColor(
                    Color.parseColor("#FFC6C6")
            );
        });

        cardCoca2.setOnClickListener(v -> {
            cocaSeleccionada = 5;
            resetCoca();
            cardCoca2.setBackgroundColor(
                    Color.parseColor("#FFC6C6")
            );
        });

        cardCoca3.setOnClickListener(v -> {
            cocaSeleccionada = 6;
            resetCoca();
            cardCoca3.setBackgroundColor(
                    Color.parseColor("#FFC6C6")
            );
        });

        cardAgua1.setOnClickListener(v -> {
            aguaSeleccionada = 1000;
            resetAgua();
            cardAgua1.setBackgroundColor(
                    Color.parseColor("#D6E9FA")
            );
        });

        cardAgua2.setOnClickListener(v -> {
            aguaSeleccionada = 2000;
            resetAgua();
            cardAgua2.setBackgroundColor(
                    Color.parseColor("#D6E9FA")
            );
        });

        cardAgua3.setOnClickListener(v -> {
            aguaSeleccionada = 3000;
            resetAgua();
            cardAgua3.setBackgroundColor(
                    Color.parseColor("#D6E9FA")
            );
        });

        btnContinuar.setOnClickListener(v -> {
            if (cocaSeleccionada == 0 || aguaSeleccionada == 0) {
                Toast.makeText(this, "Selecciona tus preferencias", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ConfiguracionInicialActivity.this, MainActivity.class);
            intent.putExtra("metaAgua", aguaSeleccionada);
            intent.putExtra("cocasDiarias", cocaSeleccionada);
            startActivity(intent);
            finish();
        });

    }
    private void resetCoca() {
        cardCoca1.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
        cardCoca2.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
        cardCoca3.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
    }

    private void resetAgua() {
        cardAgua1.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
        cardAgua2.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
        cardAgua3.setBackgroundColor(
                Color.parseColor("#D9D9D9")
        );
    }
}