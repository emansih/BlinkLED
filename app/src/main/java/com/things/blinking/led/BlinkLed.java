package com.things.blinking.led;

/**
 * Copyright(C) 2017 Daniel Quah
 * 	
 * This file is part of com.things.* package
 *
 * Blink LED is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Blink LED is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class BlinkLed extends AppCompatActivity {


    //Declare which pin is wired up.
    private String GPIO = "BCM18";

    private Gpio gpio;
    private String TAG = "BLINK";
    private Handler handler = new Handler();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    PeripheralManagerService peripheralManagerService = new PeripheralManagerService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase.goOnline();
        // Database value has to be set to true since user might run IoT app earlier than phone app
        // and *WILL* cause Null Pointer Exception later on
        databaseReference.child("LED Status").setValue("True");

        //Setup GPIO connection
        try{
            gpio = peripheralManagerService.openGpio(GPIO);
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            handler.post(blinkRunnable);
        } catch (IOException ioe){
            Log.e(TAG, "Error on PeripheralIO API(onCreate)", ioe);
        }
    }

    //Close and free up resource
    @Override
    protected void onDestroy(){
        super.onDestroy();
        firebaseDatabase.goOffline();
        if(gpio != null){
            try{
                gpio.close();
            } catch (IOException ioe){
                Log.e(TAG, "Error on Peripheral IO API", ioe);
            }
        }
    }


    // Background thread
    private Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (gpio == null){
                return;
            }
             ValueEventListener ledListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get LED Status and use the value
                        String databaseValue = dataSnapshot.getValue().toString();
                        if(databaseValue.equals("LED Status=True")) {
                            try {
                                gpio.setValue(gpio.getValue());
                            } catch (IOException ioe) {
                                Log.e(TAG, "Error on Peripheral IO API", ioe);
                            }
                        }else{
                            try {
                                gpio.setValue(!gpio.getValue());
                            } catch (IOException ioe){
                                Log.e(TAG, "Error on Peripheral IO API", ioe);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                databaseReference.addValueEventListener(ledListener);

        }
    };


}




