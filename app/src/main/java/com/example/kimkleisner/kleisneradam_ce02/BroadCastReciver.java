package com.example.kimkleisner.kleisneradam_ce02;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//Adam Kleinser
//Term number: 1702
//KleisnerAdam_CE02

public class BroadCastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getFlags();
    }
}
