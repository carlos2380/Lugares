package com.example.mislugares;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mislugares.MisLugaresJava.Lugar;
import com.example.mislugares.MisLugaresJava.Lugares;
import com.example.mislugares.MisLugaresJava.TipoLugar;

import java.text.DateFormat;
import java.util.Date;

public class EdicionLugar extends AppCompatActivity {

    private Long id;
    private int idInt;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_lugar);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        idInt = id.intValue();
        lugar = Lugares.elemento(idInt);

        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());

        tipo = (Spinner) findViewById(R.id.tipo);

        direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getdireccion());


        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText(String.valueOf(lugar.getTelefono()));

        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());

        comentario = (EditText) findViewById(R.id.url);
        comentario.setText(lugar.getUrl());

        tipo = (Spinner) findViewById(R.id.tipo);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());

    }

    private void guardar() {
        lugar.setNombre(nombre.getText().toString());
        lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
        lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
        lugar.setUrl(url.getText().toString());
        lugar.setComentario(comentario.getText().toString());
        finish();
    }
    public void lanzarVistaLugar(View view){
        Intent i = new Intent(this, VistaLugar.class);
        i.putExtra("id", (long) 0);
        startActivity(i);
    }

    //Colocar el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    //Si se clica una opcion del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar:
                guardar();
                Lugares.actualizarLugar(idInt, lugar);
                break;
            case R.id.cancelar:
                finish();
        }
        return true;
    }
}
