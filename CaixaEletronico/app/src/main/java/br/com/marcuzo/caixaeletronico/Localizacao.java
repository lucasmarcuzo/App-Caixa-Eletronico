package br.com.marcuzo.caixaeletronico;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class Localizacao {
    public static double latitude;
    public static double longitude;

    private static Timer timer;
    static TimerTask timerTask;

    //Exibe a localização do usuário no mapa.
    public static void encontrarPosicao(Activity activity, WebView wv_googleMaps) {

        // Se já deu permissão, obtem a localização
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;

        }
        else
        {
            LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(activity, "GPS Desligado!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onProviderEnabled(String provider) {
                    //Toast.makeText(MinhaLocalizacao.this, "GPS Ligado", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //Sem uso
                }
            });

            //Exibe a posição no mapa.
            timer = new Timer();
            Toast.makeText(activity, "Carregando...", Toast.LENGTH_LONG).show();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            if(latitude != 0 && longitude != 0)
                            {
                                wv_googleMaps.getSettings().setJavaScriptEnabled(true);
                                wv_googleMaps.getSettings().setBuiltInZoomControls(true);
                                wv_googleMaps.getSettings().setSupportZoom(true);
                                wv_googleMaps.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 1600);

        }
    }
}
