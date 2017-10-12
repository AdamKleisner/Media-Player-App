package com.example.kimkleisner.kleisneradam_ce02;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import java.io.IOException;
import java.util.Random;

public class AudioPlaybackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    //Adam Kleinser
    //Term number: 1702
    //KleisnerAdam_CE02

    private String[] songs;
    private String[] songsName;
    private int currentSong = 0;
    private boolean shuffle = false;
    private boolean repeat = false;

    private static final int STATE_IDLE = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_PREPARING = 2;
    private static final int STATE_PREPARED = 3;
    private static final int STATE_STARTED = 4;
    private static final int STATE_PAUSED = 5;
    private static final int STATE_STOPPED = 6;
    private static final int STATE_PLAYBACK_COMPLETED = 7;
    private static final int STATE_END = 8;

    private static final int NOTIFICATION_ID = 0X01001;

    private int mState;

    private MediaPlayer mPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AudioServiceBinder();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mState = STATE_PREPARED;

        mPlayer.start();
        mState = STATE_STARTED;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mPlayer.reset();

        if(!repeat) {

            currentSong = currentSong + 1;
            if (currentSong >= songs.length - 1) {
                currentSong = 0;
            }
        }else if (shuffle){

               Random r = new Random();
               int randomNumber = (r.nextInt(3));

            while (randomNumber == currentSong){
                randomNumber = (r.nextInt(3));
            }

            currentSong = randomNumber;
        }

        settingUpSong();

    }

    public class AudioServiceBinder extends Binder {
        AudioPlaybackService getService(){return AudioPlaybackService.this;}
    }

    @Override
    public void onCreate() {
        super.onCreate();

        songsName = new String[] {"Aint He Sweet", "Naughty Hula Eyes", "Something Elated"};
        songs = new String[] {"android.resource://" + getPackageName() + "/" + R.raw.ainthesweet,
                "android.resource://" + getPackageName() + "/" + R.raw.andy_lona_naughty_hula_eyes,
                "android.resource://" + getPackageName() + "/" + R.raw.something_elated};

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mState = STATE_IDLE;
        mPlayer.setOnPreparedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPlayer.release();
        mState = STATE_END;
    }

    public void next(){
        if (shuffle){
            Random r = new Random();
            int randomNumber = (r.nextInt(3));

            while (randomNumber == currentSong){
                randomNumber = (r.nextInt(3));
            }

            currentSong = randomNumber;

        }else {
            currentSong = currentSong + 1;
            if (currentSong >= songs.length) {
                currentSong = 0;
            }
        }

        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer.reset();

        settingUpSong();

    }

    public void back(){
        if(shuffle){
            Random r = new Random();
            int randomNumber = (r.nextInt(3));

            while (randomNumber == currentSong){
                randomNumber = (r.nextInt(3));
            }

            currentSong = randomNumber;
        }else {
            currentSong = currentSong - 1;
            if (currentSong == -1) {
                currentSong = 2;
            }
        }

        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer.reset();

        settingUpSong();
    }

    public void repeat(){
        if(repeat){

            repeat = false;

        }else{

            repeat = true;
        }
    }

    public void shuffle(){
        if(shuffle){

            shuffle = false;

        }else{

            shuffle = true;
        }
    }

    public void play(){
        if(mState == STATE_PAUSED){
            mPlayer.start();
            mState = STATE_STARTED;

        }else if(mState != STATE_STARTED && mState != STATE_PREPARED){

            mPlayer.reset();
            mState = STATE_IDLE;

            try{
                Uri songUri = Uri.parse(songs[currentSong]);
                mPlayer.setDataSource(this, songUri);
                mState = STATE_INITIALIZED;

            }catch(IOException e){
                e.printStackTrace();
            }

            if(mState == STATE_INITIALIZED){

                mPlayer.prepareAsync();
                mState = STATE_PREPARING;
            }

            Notification ongoing = buildNotification();
            startForeground(NOTIFICATION_ID,ongoing);

        }
    }

    public void pause(){

        if(mState == STATE_STARTED){

            mPlayer.pause();
            mState = STATE_PAUSED;
        }
    }

    public void stop(){

        if(mState == STATE_STARTED || mState == STATE_PAUSED || mState == STATE_PLAYBACK_COMPLETED){
            mPlayer.stop();
            mState = STATE_STOPPED;

            stopForeground(true);
        }
    }

    private Notification buildNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_music_note);
        builder.setContentTitle(songsName[currentSong]);
        builder.setContentText("Click to reopen app");
        builder.setOngoing(true);

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this,0,activityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(activityPendingIntent);

        Intent nextIntent = new Intent(this, BroadCastReciver.class);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this,1,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent backIntent = new Intent(this, BroadCastReciver.class);
        PendingIntent backPendingIntent = PendingIntent.getBroadcast(this,2,backIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent);
        builder.addAction(android.R.drawable.ic_media_previous,"Back", backPendingIntent);


        NotificationCompat.BigPictureStyle bigNotifitcation = new NotificationCompat.BigPictureStyle();
        bigNotifitcation.setBigContentTitle(songsName[currentSong]);
        bigNotifitcation.setSummaryText("Tap the notification to reopen the app");

        builder.setStyle(bigNotifitcation);

        return builder.build();
    }

    private void settingUpSong() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.reset();

        try {
            Uri songUri = Uri.parse(songs[currentSong]);
            mPlayer.setDataSource(this, songUri);
            mState = STATE_INITIALIZED;

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();

        Notification ongoing = buildNotification();
        startForeground(NOTIFICATION_ID,ongoing);
    }
}
