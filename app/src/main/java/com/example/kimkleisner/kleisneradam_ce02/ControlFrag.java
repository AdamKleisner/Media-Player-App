package com.example.kimkleisner.kleisneradam_ce02;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ControlFrag extends Fragment implements View.OnClickListener {

    //Adam Kleinser
    //Term number: 1702
    //KleisnerAdam_CE02

    public static ControlFrag newInstance() {

        return new ControlFrag();
    }



    public interface PlaybackCommandListener{
        void play();
        void pause();
        void stop();
        void next();
        void back();
        void shuffle();
        void repeat();
    }

    private PlaybackCommandListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PlaybackCommandListener){
            mListener = (PlaybackCommandListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.controls_layout,container,false);
    }

    @Override
    public void onClick(View view) {
        if(mListener == null){
            return;
        }

        if(view.getId() == R.id.imageButton_play){
            mListener.play();
        }else if(view.getId() == R.id.imageButton_pause){
            mListener.pause();
        }else if(view.getId() == R.id.imageButton_stop){
            mListener.stop();
        }else if(view.getId() == R.id.imageButton_next){
            mListener.next();
        }else if(view.getId() == R.id.imageButton_back){
            mListener.back();
        }else if(view.getId() == R.id.checkBox_shuffle){
            mListener.shuffle();
        }else if(view.getId() == R.id.checkBox_repeat){
            mListener.repeat();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View root = getView();
        if(root != null){
            root.findViewById(R.id.imageButton_play).setOnClickListener(this);
            root.findViewById(R.id.imageButton_pause).setOnClickListener(this);
            root.findViewById(R.id.imageButton_stop).setOnClickListener(this);
            root.findViewById(R.id.imageButton_next).setOnClickListener(this);
            root.findViewById(R.id.imageButton_back).setOnClickListener(this);
            root.findViewById(R.id.checkBox_repeat).setOnClickListener(this);
            root.findViewById(R.id.checkBox_shuffle).setOnClickListener(this);

        }

    }
}
