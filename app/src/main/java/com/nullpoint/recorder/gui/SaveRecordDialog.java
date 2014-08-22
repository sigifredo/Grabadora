package com.nullpoint.recorder.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

public class SaveRecordDialog extends AlertDialog {

    public static final int ACCEPT = 1;
    public static final int CANCEL = 1;

    private int mButtonPressed;
    private EditText mEditText;
    private String mFileName;

    public SaveRecordDialog(Context context) {
        super(context);

        mButtonPressed = CANCEL;
        mEditText = new EditText(context);
        mFileName = "";
        SaveListener listener = new SaveListener(this);

        setView(mEditText);
        setTitle("Guardar archivo");
        setMessage("Nombre del archivo");
        setCancelable(false);
        setButton(BUTTON_POSITIVE, context.getText(android.R.string.yes), listener);
        setButton(BUTTON_NEGATIVE, context.getText(android.R.string.cancel), listener);
    }

    public int getButtonPressed() {
        return mButtonPressed;
    }

    public String getFileName() {
        return mFileName;
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
                mDialog.mButtonPressed = CANCEL;
        }

        protected void save() {
            mDialog.mFileName = mDialog.mEditText.getText().toString();

            if (mDialog.mFileName.isEmpty())
                mDialog.mFileName = DateFormat.getDateTimeInstance().format(new Date());

            mDialog.mButtonPressed = ACCEPT;
            /*
            try {
                if (mRecordFragment.mRecorder.saveRecord(fileName))
                    Toast.makeText(getActivity(), "Se ha guardaro el archivo satisfactoriamente.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_SHORT).show();
            } catch (SaveException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            */
        }
    }
}