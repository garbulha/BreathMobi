package com.example.admin.uber_tcc_sqlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.admin.uber_tcc_sqlite.R;
import android.os.Bundle;
import android.widget.Button;

import com.example.admin.uber_tcc_sqlite.banco.Constantes;
import com.uber.sdk.android.rides.RequestButton;
import com.uber.sdk.android.rides.RideParameters;


/**
 * Created by Admin on 08/03/2016.
 */
public class ApiUber extends AppCompatActivity {
    private static final String DROPOFF_ADDR = "";
    private static float DROPOFF_LAT = -23.5384581069176f;
    private static float DROPOFF_LONG = -46.43885709999995f;
    private static final String DROPOFF_NICK = "";

    private static final String PICKUP_ADDR = "";
    private static float PICKUP_LAT = -23.5384581069176f;
    private static float PICKUP_LONG = -46.43885709999995f;
    private static final String PICKUP_NICK = "";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telauber);

        DROPOFF_LAT = getIntent().getFloatExtra(Constantes.LATITUDE, 0);
        DROPOFF_LONG = getIntent().getFloatExtra(Constantes.LONGITUDE, 0);

        PICKUP_LAT = getIntent().getFloatExtra(Constantes.LATITUDE, 0);
        PICKUP_LONG = getIntent().getFloatExtra(Constantes.LONGITUDE, 0);


        String clientId = getString(R.string.client_id);
        if (clientId.equals("insert_your_client_id_here")) {
            throw new IllegalArgumentException("Please enter your client ID in client_id in res/values/strings.xml");
        }

        RequestButton uberButtonBlack = (RequestButton) findViewById(R.id.uber_button_black);

        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId(UBERX_PRODUCT_ID)
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();

        uberButtonBlack.setRideParameters(rideParameters);
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mIntent);
        finish();
    }
}
