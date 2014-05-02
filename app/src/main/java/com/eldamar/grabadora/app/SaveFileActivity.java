package com.eldamar.grabadora.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


// TODO: mostrar mensaje que no se pudo guardar el archivo al cierre de esta actividad.
public class SaveFileActivity extends ActionBarActivity {

    private File mFile;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file);

        mFile = new File(getIntent().getExtras().getString("mFilePath"));
        mEditText = (EditText) findViewById(R.id.et1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings)
            return true;
        else
            return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        String fileName = mEditText.getText().toString().trim();

        if (fileName.isEmpty())
            Toast.makeText(this, "Por favor, especifique un nombre de archivo.", Toast.LENGTH_LONG).show();
        else {
            // TODO: obtener la verdadera extensión.
            fileName = fileName + ".3pg";

            File oFile = new File(getFilesDir(), fileName);

            if (mFile.renameTo(oFile))
                Toast.makeText(this, "Se ha guardaro el archivo satisfactoriamente." + oFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Ha ocurrido un problema, y el archivo no pudo ser guardado.", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    public void cancel(View view) {
        mFile.deleteOnExit();
        finish();
    }
}
