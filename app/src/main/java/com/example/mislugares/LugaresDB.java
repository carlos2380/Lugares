package com.example.mislugares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import com.example.mislugares.MisLugaresJava.TipoLugar;

/**
 * Created by carlos on 28/06/2016.
 */
public class LugaresDB extends SQLiteOpenHelper {

    public LugaresDB(Context context) {
        super(context, "lugares", null, 1);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE lugares(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "direccion TEXT, " +
                "longitud REAL, " +
                "latitud REAL, " +
                "tipo INTEGER, " +
                "foto TEXT, " +
                "telefono INTENGER, " +
                "url TEXT, " +
                "comentario TEXT, " +
                "fecha LONG, " +
                "valoracion REAL)"
        );

        bd.execSQL("INSERT INTO lugares VALUES (null, " +
                "'Escuela Politécnica Superior', " +
                "'C/ Paranfin, 1 4562 Gandia (SPAIN)', -0.166093, 38.995656, " +
                TipoLugar.EDUCACION.ordinal() +", '', 962849300, " +
                "'http://www.epsg.upv.es' ," +
                "'Uno de los lugares para formarse', " +
                System.currentTimeMillis() + ", 3.0)"
        );

        bd.execSQL("INSERT INTO lugares VALUES (null, " +
                "'Campo futbol', " +
                "'C/ Real', -0.1671, 39.125, " +
                TipoLugar.DEPORTE.ordinal() +", '', 934849300, " +
                "'http://www.google.es' ," +
                "'Madrid', " +
                System.currentTimeMillis() + ", 5.0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Cursor listado(){
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery("SELECT * FROM lugares", null);
    }


}
