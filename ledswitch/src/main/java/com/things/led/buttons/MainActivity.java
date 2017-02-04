package com.things.led.buttons;

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
 
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Go online and start database
        firebaseDatabase.goOnline();

        // Set database as True first
        databaseReference.child("LED Status").setValue("True");
        buttonListener();
    }

    public void buttonListener(){
       toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        //LED is turned on by default
        toggleButton.setChecked(true);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Two states: True, False
                if(compoundButton.isChecked()){
                    // True
                    // Set to database to "True"
                    databaseReference.child("LED Status").setValue("True");
                } else{
                    // False
                    // Setto database to "False"
                    databaseReference.child("LED Status").setValue("False");
                }

            }
        });
    }
    // Close connection
    public void onDestroy(){
        firebaseDatabase.goOffline();
        super.onDestroy();
    }

    public void onPause(){
        firebaseDatabase.goOffline();
        super.onPause();
    }

    public void onResume(){
        firebaseDatabase.goOnline();
        super.onResume();
    }
}
