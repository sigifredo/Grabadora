package com.caguapp.grabadora.app;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class RecordFragment extends Fragment implements View.OnClickListener {

    // TODO: Crear una propioa clase MediaRecorder.
    private Button mButton;
    private Chronometer mChronometer;
    private File mFile;
    private MediaRecorder mRecorder;
    private RadioButton m3pg;
    private RadioButton mMp3;

    public final static int F_3PG = 1;
    public final static int F_MP3 = 2;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        mButton = (Button) view.findViewById(R.id.button);
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        m3pg = (RadioButton) view.findViewById(R.id.rb3pg);
        mMp3 = (RadioButton) view.findViewById(R.id.rbmp3);

        mButton.setOnClickListener(this);
        mFile = null;
        mRecorder = null;

        int format = getActivity().getSharedPreferences("configs", Context.MODE_PRIVATE).getInt("format", F_3PG);

        if (format == F_3PG)
            m3pg.setChecked(true);
        else if (format == F_MP3)
            mMp3.setChecked(true);

        return view;
    }

    @Override
    public void onClick(View view) {
        boolean b3pg = m3pg.isChecked();
        final String format = b3pg?".3pg":".mp3";

        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            if (b3pg)
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

                mButton.setText(R.string.stop);

            } catch (IOException exception) {
                Toast.makeText(getActivity(), "No se ha podido iniciar la grabación.", Toast.LENGTH_SHORT).show();
                mRecorder = null;
            }
        } else {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            mButton.setText(R.string.record);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final EditText et = new EditText(getActivity());
            builder.setView(et)
                    .setTitle("Guardar archivo")
                    .setMessage("Nombre del archivo")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int iButton) {
                            String fileName = et.getText().toString().trim();

                            if (fileName.isEmpty())
                                fileName = DateFormat.getDateTimeInstance().format(new Date());

                            if (!fileName.contains(format))
                                fileName = fileName + format;

                            File oFile = new File(getActivity().getFilesDir(), fileName);

                            // TODO: revisar que el archivo no exista
                            if (mFile.renameTo(oFile))
                                Toast.makeText(getActivity(), "Se ha guardaro el archivo satisfactoriamente.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int iButton) {
                            mFile.delete();
                            mFile = null;
                        }
                    })
                    .create().show();
        }
    }
}
