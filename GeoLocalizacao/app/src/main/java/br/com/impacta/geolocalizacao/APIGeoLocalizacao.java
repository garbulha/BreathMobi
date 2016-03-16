package br.com.impacta.geolocalizacao;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.api.GoogleApiClient.*;

/**
 * Created by TGarbulha on 13/03/2016.
 */
public class APIGeoLocalizacao implements ConnectionCallbacks, OnConnectionFailedListener {

    private Context context;
    private GoogleApiClient APIGoogle;
    public APIGeoLocalizacao(Context context) {
        this.context = context;

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private synchronized void ConnectionGPS(){
        APIGoogle = new


                Builder(context) {
        }

    }
}
