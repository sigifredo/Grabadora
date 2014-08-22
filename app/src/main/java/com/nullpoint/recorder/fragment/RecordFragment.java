package com.nullpoint.recorder.fragment;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.nullpoint.recorder.exceptions.StartException;
import com.nullpoint.recorder.gui.R;
import com.nullpoint.recorder.gui.SaveRecordDialog;
import com.nullpoint.recorder.util.Recorder;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class RecordFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Chronometer mChronometer;
    private Recorder mRecorder;

    public RecordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        mButton = (Button) view.findViewById(R.id.button);
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        RadioButton m3pg = (RadioButton) view.findViewById(R.id.rb3pg);
        RadioButton mMp3 = (RadioButton) view.findViewById(R.id.rbmp3);

        mRecorder = new Recorder();

        Recorder.Format format = getFormatFromPreferences();
        mButton.setOnClickListener(this);
        mRecorder.setFormat(format);

        if (format == Recorder.Format._3pg)
            m3pg.setChecked(true);
        else if (format == Recorder.Format.Mp3)
            mMp3.setChecked(true);

        m3pg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    changeFormat(Recorder.Format._3pg);
            }
        });

        mMp3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    changeFormat(Recorder.Format.Mp3);
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
        if (mRecorder.isRecording()) {
            mChronometer.stop();
            mRecorder.stopRecord();
            mButton.setText(R.string.record);
            mChronometer.setBase(SystemClock.elapsedRealtime());

            SaveRecordDialog saveRecordDialog = new SaveRecordDialog(getActivity(), mRecorder);
            saveRecordDialog.show();
        } else {
            try {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mRecorder.startRecord();
            } catch (StartException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeFormat(Recorder.Format format) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("configs", Context.MODE_PRIVATE).edit();
        editor.putInt("format", format.ordinal());
        editor.apply();

        mRecorder.setFormat(format);
    }

    public Recorder.Format getFormatFromPreferences() {
        int format = getActivity().getSharedPreferences("configs", Context.MODE_PRIVATE).getInt("format", -1);
        if (format == -1) {
            Recorder.Format recorderFormat = Recorder.Format.values()[0];

            changeFormat(recorderFormat);

            return recorderFormat;
        } else
            return Recorder.Format.values()[--format];
    }
}
