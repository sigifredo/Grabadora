package com.eldamar.grabadora.app;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PlayFragment extends Fragment implements View.OnClickListener {

    // TODO: Crear una propioa clase MediaRecorder.
    private MediaPlayer mMediaPlayer;
    private String mFilePath;

    public PlayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        Button playButton = (Button) view.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        mMediaPlayer = new MediaPlayer();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            mMediaPlayer.setDataSource(mFilePath);
            mMediaPlayer.prepare();
        } catch (IOException ex) {
            mMediaPlayer = null;
        }
    }

    public void setRecordPath(String path) {
        mFilePath = path;
    }

    public void onClick(View view) {
        start();
    }

    public void start() {
        if (mMediaPlayer == null)
            Toast.makeText(getActivity(), "No se ha podido reproducir el archivo", Toast.LENGTH_LONG).show();
        else if (!mMediaPlayer.isPlaying())
            mMediaPlayer.start();
    }
}
