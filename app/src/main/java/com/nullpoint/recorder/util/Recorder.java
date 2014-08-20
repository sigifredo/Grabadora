package com.nullpoint.recorder.util;

import android.app.AlertDialog;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.nullpoint.recorder.exceptions.StartException;

class Recorder {

    enum Format {Mp3, _3pg}

    private Format mFormat;
    private Chronometer mChronometer;
    private File mFile;
    private MediaRecorder mRecorder;

    public Recorder() {
    }

    protected String formatString() {
        String sFormat;

        switch (mFormat) {
        case _3pg:
            sFormat = ".3pg";
            break;
        case Mp3:
            sFormat = ".mp3";
            break;
        default:
            sFormat = ".xxx";
        }

        return sFormat;
    }

    public boolean isRecording() {
        return false;
    }

    public boolean saveRecord() {
        return saveRecord("");
    }

    public boolean saveRecord(String path) {

        String sFormat;

        if (path.isEmpty())
            path = DateFormat.getDateTimeInstance().format(new Date());

        sFormat = formatString();

        if (!path.contains(sFormat))
            path = path + sFormat;

        // File oFile = new File(getActivity().getFilesDir(), fileName);
        File oFile = new File(path);

        mFile.setReadable(true, false);
        // TODO: revisar que el archivo no exista
        if (mFile.renameTo(oFile))
            // Toast.makeText(getActivity(), "Se ha guardaro el archivo satisfactoriamente.", Toast.LENGTH_SHORT).show();
            return true;
        else
            // Toast.makeText(getActivity(), "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_SHORT).show();
            return false;
    }

    public void startRecord() throws StartException {
        if (isRecording()) {
            throw new StartException("Ya hay una grabación en curso.");
        } else {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            switch (mFormat) {
            case _3pg:
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                break;
            case Mp3:
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                break;
            }

            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mFile = File.createTempFile("tmp", format);
                mRecorder.setOutputFile(mFile.getAbsolutePath());
                mRecorder.prepare();
                mRecorder.start();
                mChronometer.start();
            } catch (IOException exception) {
                mRecorder = null;
                throw new StartException("No se ha podido iniciar la grabación.");
                // Toast.makeText(getActivity(), "No se ha podido iniciar la grabación.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopRecord() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        mButton.setText(R.string.record);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText et = new EditText(getActivity());

        /*
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int iButton) {
                        mFile.delete();
                        mFile = null;
                    }
                })
                .create().show();
        */
    }
}