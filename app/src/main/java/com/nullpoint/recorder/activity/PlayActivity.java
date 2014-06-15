package com.nullpoint.recorder.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.nullpoint.recorder.gui.R;

import java.io.IOException;

public class PlayActivity extends Activity implements Runnable {

    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Button playButton = (Button) findViewById(R.id.playButton);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        // playButton.setOnClickListener(this);
        mFilePath = getIntent().getExtras().getString("filePath");

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mFilePath);
            mMediaPlayer.prepare();
        } catch (IOException ex) {
            mMediaPlayer = null;
        }
    }

    public void start(View view) {
        if (mMediaPlayer == null)
            Toast.makeText(this, "No se ha podido reproducir el archivo", Toast.LENGTH_LONG).show();
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
}