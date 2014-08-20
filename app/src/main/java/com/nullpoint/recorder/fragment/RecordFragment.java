package com.nullpoint.recorder.fragment;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nullpoint.recorder.gui.R;


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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        m3pg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    saveFormat(F_3PG);
            }
        });

        mMp3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    saveFormat(F_MP3);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onClick(View view) {
        boolean b3pg = m3pg.isChecked();
        final String format = b3pg?".3pg":".mp3";

        if (mRecorder == null) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
        } else {
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
        }
    }

    public void saveFormat(int format) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("configs", Context.MODE_PRIVATE).edit();
        editor.putInt("format", format);
        editor.commit();
    }
}
