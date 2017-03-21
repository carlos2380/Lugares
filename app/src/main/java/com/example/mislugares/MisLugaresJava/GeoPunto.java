package com.example.mislugares.MisLugaresJava;

/**
 * Created by carlos on 23/06/2016.
 */
public class GeoPunto {
    private double longitud, latitud;

    public GeoPunto(double longitud, double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    //El método distancia recibe como parámetro
    //un Objeto del tipo GeoPunto
    public double distancia(GeoPunto punto)
    {
        /*Podemos utilizar el algoritmo Haversine
        //esta funcion nos sirve para calcular la distancia entre dos puntos:
        los cuales serán la latitud del propio objeto 'this...'
        y el objeto GeoPunto pasado 'punto'*/

        final double RADIO_TIERRA = 6371000; // en metros

        double dLat = Math.toRadians(this.latitud - punto.latitud);

        double dLon = Math.toRadians(this.longitud - punto.longitud);

        double lat1 = Math.toRadians(punto.latitud);

        double lat2 = Math.toRadians(this.latitud);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +

                Math.sin(dLon/2) * Math.sin(dLon/2) *

                        Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return c * RADIO_TIERRA;
    }
}
