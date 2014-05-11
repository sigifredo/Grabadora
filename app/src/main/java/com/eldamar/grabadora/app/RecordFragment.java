package com.eldamar.grabadora.app;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mFile = null;
        mRecorder = null;

        return view;
    }

    @Override
    public void onClick(View view) {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mFile = File.createTempFile("tmp", ".3pg");
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
            View dialogSave = getActivity().getLayoutInflater().inflate(R.layout.dialog_save, null);
            final EditText et = (EditText) dialogSave.findViewById(R.id.editText);
            builder.setView(dialogSave)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int iButton) {
                            String fileName = et.getText().toString().trim();

                            if (fileName.isEmpty()) {
                                // TODO: no cerrar el diálogo.
                                Toast.makeText(getActivity(), "El nombre del archivo no es correcto.", Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO: obtener la verdadera extensión.
                                if (!fileName.contains(".3pg"))
                                    fileName = fileName + ".3pg";

                                File oFile = new File(getActivity().getFilesDir(), fileName);

                                if (mFile.renameTo(oFile))
                                    Toast.makeText(getActivity(), "Se ha guardaro el archivo satisfactoriamente.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(), "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_SHORT).show();
                            }
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
