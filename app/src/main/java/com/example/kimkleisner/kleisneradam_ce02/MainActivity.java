package com.example.kimkleisner.kleisneradam_ce02;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ServiceConnection,
        ControlFrag.PlaybackCommandListener {

    //Adam Kleinser
    //Term number: 1702
    //KleisnerAdam_CE02

    private AudioPlaybackService mService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.main_frame,ControlFrag.newInstance()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mBound = false;
        Intent serviceIntent = new Intent(this, AudioPlaybackService.class);
        bindService(serviceIntent,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mBound = false;
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        AudioPlaybackService.AudioServiceBinder binder = (AudioPlaybackService.AudioServiceBinder) iBinder;

        mService = binder.getService();
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBound = false;
    }

    @Override
    public void play() {
        if(mBound){
            mService.play();

            Intent serviceIntent = new Intent(this, AudioPlaybackService.class);
            startService(serviceIntent);
        }
    }

    @Override
    public void pause() {
        if(mBound){
            mService.pause();
        }
    }

    @Override
    public void stop() {
        if(mBound){
            mService.stop();
        }

        Intent serviceIntent = new Intent(this,AudioPlaybackService.class);
        stopService(serviceIntent);
    }

    @Override
    public void next() {
        if(mBound){
            mService.next();
        }
    }

    @Override
    public void back() {
        if(mBound){
            mService.back();
        }
    }

    @Override
    public void shuffle() {
        if(mBound){
            mService.shuffle();
        }
    }

    @Override
    public void repeat() {
        if(mBound){
            mService.repeat();
        }
    }
}
