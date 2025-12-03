package com.example.appmensajeria;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onSupportNavigateUp() {
        return androidx.navigation.Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigateUp() || super.onSupportNavigateUp();
    }

}
