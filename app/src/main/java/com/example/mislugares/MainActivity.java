package com.example.mislugares;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mislugares.MisLugaresJava.Lugares;


public class MainActivity extends ListActivity implements LocationListener {

    private static final long DOS_MINUTOS = 2 * 60 * 1000;

    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;
    public AdaptadorCursorLugares adaptador;

    //GPS
    private LocationManager manejador;
    private Location mejorLocaliz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adaptador = new AdaptadorLugares(this);
        Lugares.inicializaDB(getApplicationContext());
        //adaptador = new AdaptadorLugares(this, R.layout.elemento_lista, Lugares.listado(), new String[] {"nombre", "direccion"}, new int[] {R.id.nombre, R.id.direccion},0);
        adaptador = new AdaptadorCursorLugares(this, Lugares.listado());
        setListAdapter(adaptador);

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            actualizarMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            actualizarMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activarProveedores();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manejador.removeUpdates((LocationListener) this);
    }


    private void activarProveedores() {
        if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20 * 1000, 5, (LocationListener) this);
        }
        if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, (LocationListener) this);
        }
    }

    private void actualizarMejorLocaliz(Location localiz){
        if(localiz != null && (mejorLocaliz == null)
                || localiz.getAccuracy() < 2*mejorLocaliz.getAccuracy()
                || localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS) {
            Log.d(Lugares.TAG, "Nueva mejor localizacion");
            mejorLocaliz = localiz;
            Lugares.posicionActual.setLatitud(localiz.getLatitude());
            Lugares.posicionActual.setLongitud(localiz.getLongitude());
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View vista, int
            position, long id) {
        super.onListItemClick(listView, vista, position, id);
        Intent i = new Intent(this, VistaLugar.class);
        i.putExtra("id",id);
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Lugares.TAG, "Nueva localizacion" + location);
        actualizarMejorLocaliz(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(Lugares.TAG, "Cambia estado" + s);
        activarProveedores();
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(Lugares.TAG, "Se habilitan" + s);
        activarProveedores();
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(Lugares.TAG, "Se deshabilita" + s);
        activarProveedores();
    }

    //Crea el menu actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Si se clica una opcion del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_mapa:
                Intent intent = new Intent(this, Mapa.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    /*public void lanzarVistaLugar(View view){
        Intent i = new Intent(this, VistaLugar.class);
        i.putExtra("id", (long) 0);
        startActivity(i);
    }

    //Colocar el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Si se clica una opcion del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_buscar:
                lanzarVistaLugar(null);
                break;
        }
        return true;
    }*/



}
