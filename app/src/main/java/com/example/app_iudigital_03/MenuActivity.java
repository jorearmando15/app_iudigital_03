package com.example.app_iudigital_03;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MenuActivity extends AppCompatActivity {

    private static final String EXTRA_APPS = "apps";
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lista = findViewById(R.id.listamenu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String valor = extras.getString("extra");
            if (EXTRA_APPS.equals(valor)) {
                lista.setAdapter(new AppAdapter(this));
            } else {
                lista.setAdapter(new CategoryAdapter(this));
            }
        }
    }
}
