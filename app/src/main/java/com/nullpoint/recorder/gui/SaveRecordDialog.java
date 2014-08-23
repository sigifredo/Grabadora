package com.nullpoint.recorder.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.nullpoint.recorder.exceptions.SaveException;
import com.nullpoint.recorder.util.Recorder;

import java.text.DateFormat;
import java.util.Date;

public class SaveRecordDialog extends AlertDialog {

    private EditText mEditText;
    private Recorder mRecorder;

    public SaveRecordDialog(Context context, Recorder recorder) {
        super(context);

        mEditText = new EditText(context);
        mRecorder = recorder;
        SaveListener listener = new SaveListener(this);

        setView(mEditText);
        setTitle("Guardar archivo");
        setMessage("Nombre del archivo");
        setCancelable(false);
        setButton(BUTTON_POSITIVE, context.getText(android.R.string.yes), listener);
        setButton(BUTTON_NEGATIVE, context.getText(android.R.string.cancel), listener);
    }

    class SaveListener implements DialogInterface.OnClickListener {

        private SaveRecordDialog mDialog;

        public SaveListener(SaveRecordDialog dialog) {
            mDialog = dialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE)
                save();
            else
                mDialog.mRecorder.clearCache();
        }

        protected void save() {
            String dir = mDialog.getContext().getFilesDir() + "/";
            String fileName = mDialog.mEditText.getText().toString().trim();

            if (fileName.isEmpty())
                fileName = dir + DateFormat.getDateTimeInstance().format(new Date());
            else
                fileName = dir + fileName;

            try {
                if (mDialog.mRecorder.saveRecord(fileName))
                    Toast.makeText(mDialog.getContext(), "Se ha guardaro el archivo satisfactoriamente.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mDialog.getContext(), "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_SHORT).show();
            } catch (SaveException e) {
                Toast.makeText(mDialog.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}