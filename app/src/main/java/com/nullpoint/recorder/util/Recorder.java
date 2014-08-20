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

    private boolean mB3pg;
    private Chronometer mChronometer;
    private File mFile;
    private MediaRecorder mRecorder;

    public Recorder() {
    }

    public boolean isRecording() {
        return false;
    }

    public boolean saveRecord() {
        return saveRecord("");
    }

    public boolean saveRecord(String path) {

        if (path.isEmpty())
            path = DateFormat.getDateTimeInstance().format(new Date());

        if (!path.contains(format))
            path = path + format;

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
            final String format = mB3pg?".3pg":".mp3";
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            if (mB3pg)
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            else
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

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