package com.example.jabsa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_bienvenida);

        // Obtener referencia al botón en el diseño de la actividad
        Button btnNextActivity = findViewById(R.id.btnNextActivity);
        // Establecer un listener de clic para el botón
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para iniciar la siguiente actividad
                Intent intent = new Intent(PantallaBienvenida.this, TareaListActivity.class);
                startActivity(intent);
            }
        });
    }
}