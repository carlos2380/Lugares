package com.example.mislugares;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mislugares.MisLugaresJava.Lugar;
import com.example.mislugares.MisLugaresJava.Lugares;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;


public class VistaLugar extends AppCompatActivity {

    private Long id;
    private int idInt;
    private Lugar lugar;
    private ImageView imageView;
    final static int RESULTADO_EDITAR= 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    private Uri uriFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_lugar);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        idInt = id.intValue();

        imageView= (ImageView) findViewById(R.id.lista_foto);


        actualizarVistas();

    }

    private void actualizarVistas() {
        lugar = Lugares.elemento(idInt);
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());

        ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());

        TextView tipo = (TextView) findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());

        //Ocultar y dejar espacio si el telefono esta vacio
        if(lugar.getdireccion() == null || lugar.getdireccion().isEmpty()) {
            findViewById(R.id.lista_direccion).setVisibility(View.GONE);
        } else {
            TextView direccion = (TextView) findViewById(R.id.lista_direccion);
            direccion.setText(lugar.getdireccion());
        }


        //Ocultar y dejar espacio si el telefono esta vacio
        if(lugar.getTelefono() == 0) {
            findViewById(R.id.t_telefono).setVisibility(View.GONE);
        } else {
            TextView telefono = (TextView) findViewById(R.id.phone);
            telefono.setText(String.valueOf(lugar.getTelefono()));
        }

        if(lugar.getUrl() == null || lugar.getUrl().isEmpty()){
            findViewById(R.id.url).setVisibility(View.GONE);
        }else{
            TextView url = (TextView) findViewById(R.id.url);
            url.setText(lugar.getUrl());
        }

        if(lugar.getComentario().toString() == null || lugar.getComentario().toString().isEmpty()){
            findViewById(R.id.comentario).setVisibility(View.GONE);
        }else{
            TextView comemtario = (TextView) findViewById(R.id.comentario);
            comemtario.setText(lugar.getComentario().toString());
        }


        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())
        ));

        RatingBar valorecion = (RatingBar) findViewById(R.id.lista_valoracion);
        valorecion.setRating(lugar.getValoracion());
        valorecion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                lugar.setValoracion(v);
                Lugares.actualizarLugar(idInt, lugar);
            }
        });
        ponerFoto(imageView, lugar.getFoto());
    }


     //----------------------------------------------------------------------------
     //----------------------------------------------------------------------------
    //Lanzar la actividad
    public void lanzarEdicionLugar(){
        Intent i = new Intent(this, EdicionLugar.class);
        i.putExtra("id", (long) 0);
        startActivityForResult(i, RESULTADO_EDITAR);
    }

    //Recoger la vuelta a la actividad
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas();

            //Forzar a acutalizar la vista
            findViewById(R.id.scrollView1).invalidate();
        }else if (/* Si no a cancelado la accion y esta correcto*/requestCode == RESULTADO_GALERIA
                && resultCode == Activity.RESULT_OK) {
            lugar.setFoto(data.getDataString());
            Lugares.actualizarLugar(idInt, lugar);
            ponerFoto(imageView, lugar.getFoto());
        }else if(requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK
                && lugar!=null && uriFoto!=null) {
            lugar.setFoto(uriFoto.toString());
            Lugares.actualizarLugar(idInt, lugar);
            ponerFoto(imageView, lugar.getFoto());
        }
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------

    public void eliminarFoto(View view) {
        lugar.setFoto(null);
        Lugares.actualizarLugar(idInt, lugar);
        ponerFoto(imageView, null);
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriFoto = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "img_" + (System.currentTimeMillis() / 1000) + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        startActivityForResult(intent, RESULTADO_FOTO);
    }

    //Crea el menu actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    //Si se clica una opcion del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.accion_compartir:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, lugar.getNombre() + " - " + lugar.getUrl());
                startActivity(intent);
            case R.id.accion_llegar:
                verMapa(null);
                return  true;
            case R.id.accion_editar:
                lanzarEdicionLugar();
                return  true;
            case R.id.accion_borrar:
                lanzarDialogBorrar();
            case R.id.menu_mapa:
                Intent intent2 = new Intent(this, Mapa.class);
                startActivity(intent2);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------

    //Compruba que es una foto y la pone
    protected void ponerFoto(ImageView imageView, String uri) {
        if (uri != null) {
            imageView.setImageURI(Uri.parse(uri));
        } else{
            imageView.setImageBitmap(null);
        }
    }

    //Abrir Galeria o similar
    public void galeria(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, RESULTADO_GALERIA);
    }



    public void lanzarDialogBorrar() {
        new AlertDialog.Builder(this)
                .setTitle("Borrado de Lugar")
                .setMessage("¿Seguro que Desea eliminar este lugar?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Lugares.borrar(idInt);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
    }

    public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if(lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        }else {
            uri = Uri.parse("geo0,0?q=" + lugar.getDireccion());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
