package br.com.impacta.geolocalizacao;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by TGarbulha on 12/03/2016.
 */
public class GeoLocalizacao extends Service implements LocationListener {
    private Context cx;
    private boolean flagGPS;
    private LocationManager locationManager;
    // distancia em metros para mudar as atualizacoes
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // tempo em milissegundos para atualizacao
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private Location location;

    public GeoLocalizacao(Context context) {
        this.cx = context;
        pegarLocalizacao();
    }

    private void pegarLocalizacao() {
        try {
            locationManager = (LocationManager) cx.getSystemService(LOCATION_SERVICE);
            flagGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //Verificando Status do GPS//
            if (flagGPS == false) {
                habilitarGPS();
            }



            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    //Constantes.LATITUDE = String.valueOf(location.getLatitude());
                    //Constantes.LONGITUDE = String.valueOf(location.getLongitude());
                    double lat = location.getLatitude();
                    double longi = location.getLongitude();

                    Log.d("LAT", String.valueOf(lat));
                    Log.d("LONGI", String.valueOf(longi));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parandoGPS() {
        if (locationManager != null) {

            locationManager.removeUpdates(GeoLocalizacao.this);
        }
    }


    public void habilitarGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cx);

        alertDialog.setTitle("Serviço de Localização Desabilitado");
        alertDialog.setMessage("Ative o serviço de localização");

        // OK
        alertDialog.setPositiveButton("ATIVAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                cx.startActivity(intent);
            }
        });
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
