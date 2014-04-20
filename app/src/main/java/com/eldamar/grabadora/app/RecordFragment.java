package com.eldamar.grabadora.app;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.media.MediaRecorder;
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
public class RecordFragment extends Fragment implements View.OnClickListener {

    // TODO: Crear una propioa clase MediaRecorder.
    private MediaRecorder mRecorder;
    private File mFile;
    private Button mButton;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);

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
                mFile = File.createTempFile("tmp", ".3pg");
                mRecorder.setOutputFile(mFile.getAbsolutePath());
                mRecorder.prepare();
                mRecorder.start();

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

            // TODO: Cerrar el archivo temporal.
            Intent intent = new Intent(getActivity(), SaveFileActivity.class);
            intent.putExtra("mFilePath", mFile.getAbsolutePath());
            startActivity(intent);
        }
    }
}
