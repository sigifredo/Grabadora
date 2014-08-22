package com.nullpoint.recorder.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class SaveRecordDialog extends AlertDialog {

    private EditText mEditText;

    public SaveRecordDialog(Context context) {
        super(context);

        mEditText = new EditText(context);
        SaveListener listener = new SaveListener();
        setView(mEditText);
        setTitle("Guardar archivo");
        setMessage("Nombre del archivo");
        setCancelable(false);
        setButton(BUTTON_POSITIVE, context.getText(android.R.string.yes), listener);
        setButton(BUTTON_NEGATIVE, context.getText(android.R.string.cancel), listener);
    }

    class SaveListener implements DialogInterface.OnClickListener {

        // private RecordFragment mRecordFragment;

        public SaveListener(/*RecordFragment recordFragment*/) {
            // mRecordFragment = recordFragment;
        }

        protected void cancel() {
            // mRecordFragment.mRecorder.clearCache();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE)
                save(dialog);
            else
                cancel();
        }

        protected void save(DialogInterface dialog) {
            /*
            AlertDialog alertDialog = (AlertDialog) dialog;
            String fileName = et.getText().toString().trim();

            if (fileName.isEmpty())
                fileName = DateFormat.getDateTimeInstance().format(new Date());

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