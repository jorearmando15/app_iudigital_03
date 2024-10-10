package com.example.app_iudigital_03;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    String[] titulos_menu = {"Listado de Apps", "Categorias"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);

        ArrayAdapter adaptador = new Menu_Adapter(this,titulos_menu);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                    intent.putExtra("extra","apps");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                    intent.putExtra("extra","category");
                    startActivity(intent);
                }
            }
        });

    }
}