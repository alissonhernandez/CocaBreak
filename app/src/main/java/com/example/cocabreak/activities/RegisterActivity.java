package com.example.cocabreak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cocabreak.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etNombre, etCorreo, etPassword, etConfirmPassword;
    Button btnCrearCuenta;
    TextView tvIrLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        tvIrLogin = findViewById(R.id.tvIrLogin);

        btnCrearCuenta.setOnClickListener(v -> validarRegistro());

        tvIrLogin.setOnClickListener(v -> finish());
    }

    private void validarRegistro() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Ingresa tu nombre");
            return;
        }

        if (correo.isEmpty()) {
            etCorreo.setError("Ingresa tu correo");
            return;
        }

        if (!correo.contains("@")) {
            etCorreo.setError("Correo no válido");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa una contraseña");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, ConfiguracionInicialActivity.class);
        intent.putExtra("nombreUsuario", nombre);
        intent.putExtra("correoUsuario", correo);
        startActivity(intent);
        finish();
    }
}
