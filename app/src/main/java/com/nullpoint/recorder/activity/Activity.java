package com.nullpoint.recorder.activity;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.caguapp.grabadora.app.R;

public class Activity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            getAbout().show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public AlertDialog getAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NullPoint")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Grabadora versión: 1.0.1")
                .setPositiveButton(android.R.string.yes, null);

        return builder.create();
    }
}
