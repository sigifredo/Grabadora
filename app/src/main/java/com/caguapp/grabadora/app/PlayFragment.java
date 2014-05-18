package com.caguapp.grabadora.app;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PlayFragment extends Fragment implements View.OnClickListener, Runnable {

    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private String mFilePath;

    public PlayFragment() {
    }

    public PlayFragment(String filePath) {
        mFilePath = filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        Button playButton = (Button) view.findViewById(R.id.playButton);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
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

    public void onClick(View view) {
        start();
    }

    public void start() {
        if (mMediaPlayer == null)
            Toast.makeText(getActivity(), "No se ha podido reproducir el archivo", Toast.LENGTH_LONG).show();
        else if (!mMediaPlayer.isPlaying()) {
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.start();
            new Thread(this).start();
        }
    }

    public void run() {
        if (mMediaPlayer != null) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            int total = mMediaPlayer.getDuration();

            while (mMediaPlayer != null && currentPosition < total) {
                try {
                    Thread.sleep(1000);
                    currentPosition = mMediaPlayer.getCurrentPosition();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
                mSeekBar.setProgress(currentPosition);
            }
        }
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }
}
