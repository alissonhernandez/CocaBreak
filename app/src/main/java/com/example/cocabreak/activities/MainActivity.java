package com.example.cocabreak.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.cocabreak.R;
import com.example.cocabreak.fragments.HomeFragment;
import com.example.cocabreak.fragments.PerfilFragment;
import com.example.cocabreak.fragments.RegistrarFragment;
import com.example.cocabreak.fragments.RetosFragment;
import com.example.cocabreak.fragments.LogrosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigation = findViewById(R.id.bottomNavigation);

        cargarFragment(new HomeFragment());

        bottomNavigation.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_home){
                fragment = new HomeFragment();
            }
            else if(item.getItemId() == R.id.nav_registrar){
                fragment = new RegistrarFragment();
            }
            else if(item.getItemId() == R.id.nav_retos){
                fragment = new RetosFragment();
            }
            else if(item.getItemId() == R.id.nav_logros){
                fragment = new LogrosFragment();
            }
            else if(item.getItemId() == R.id.nav_perfil){
                fragment = new PerfilFragment();
            }

            if(fragment != null){
                cargarFragment(fragment);
            }

            return true;
        });
    }

    private void cargarFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
