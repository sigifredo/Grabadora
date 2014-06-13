package com.caguapp.grabadora.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, RecordsFragment.InteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        switch(position) {
            case 0:
                mTitle = getString(R.string.title_section1);
                fragment = new RecordFragment();
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                fragment = new RecordsFragment();
                break;
            case 2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("NullPoint")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage("Grabadora versión: 1.0.1")
                        .setPositiveButton(android.R.string.yes, null)
                        .create().show();
                break;
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Toast.makeText(this, "Acerca de...", Toast.LENGTH_LONG).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (mNavigationDrawerFragment.isDrawerOpen())
            return super.onCreateOptionsMenu(menu);
        else {
            restoreActionBar();
            return true;
        }

    }

    @Override
    public void play(String path) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("filePath", path);
        startActivity(intent);
        // getSupportFragmentManager().beginTransaction().replace(R.id.container, new PlayFragment(path)).commit();
    }

    @Override
    public void deleteRecords(String [] paths) {
        File file;

        for (int i = 0; i < paths.length; i++) {
            file = new File(paths[i]);
            if (!file.delete()) {
                Toast.makeText(this, "No se han podido eliminar las grabaciones seleccionadas.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "Grabaciones eliminadas.", Toast.LENGTH_SHORT).show();
    }
}
