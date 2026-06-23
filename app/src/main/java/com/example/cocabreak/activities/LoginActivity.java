package com.example.cocabreak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cocabreak.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edCorreo, edPassword;
    private Button btnLogin;
    private TextView tvRegistrarse;
    private TextView tvForgotPassword;
    private FirebaseAuth mAuth;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        edCorreo = findViewById(R.id.edCorreo);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        ImageView imgTogglePassword =
                findViewById(R.id.imgTogglePassword);

        imgTogglePassword.setOnClickListener(v -> {

            if (passwordVisible) {

                edPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                imgTogglePassword.setImageResource(
                        R.drawable.ic_visibility_off
                );

            } else {

                edPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                imgTogglePassword.setImageResource(
                        R.drawable.ic_visibility
                );
            }

            edPassword.setSelection(
                    edPassword.getText().length()
            );

            passwordVisible = !passwordVisible;
        });

        btnLogin.setOnClickListener(v -> iniciarSesion());

        tvRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
        tvForgotPassword.setOnClickListener(v -> {

            String correo =
                    edCorreo.getText().toString().trim();

            if (correo.isEmpty()) {

                edCorreo.setError(
                        "Ingresa tu correo primero"
                );

                return;
            }

            mAuth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Revisa tu correo para recuperar tu contraseña",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });
    }

    private void iniciarSesion() {

        String correo = edCorreo.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (correo.isEmpty()) {
            edCorreo.setError("Ingresa tu correo");
            return;
        }

        if (password.isEmpty()) {
            edPassword.setError("Ingresa tu contraseña");
            return;
        }

        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                LoginActivity.this,
                                "Bienvenido",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent = new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );

                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}