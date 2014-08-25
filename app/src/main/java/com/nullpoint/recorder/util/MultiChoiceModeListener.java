package com.nullpoint.recorder.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.nullpoint.recorder.fragment.RecordsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    RecordsFragment mRecordsFragment;
    HashSet<Integer> mItems = new HashSet<Integer>();

    public MultiChoiceModeListener(RecordsFragment recordsFragment) {
        mRecordsFragment = recordsFragment;
    }

    public void deleteRecords() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRecordsFragment.getActivity());
        DialogListener listener = new DialogListener(this);

        builder.setMessage("¿Está seguro de querer eliminar las grabaciones?")
        .setTitle("Eliminar archivos")
        .setPositiveButton(android.R.string.ok, listener)
        .setNegativeButton(android.R.string.cancel, null)
        .create().show();
    }

    public boolean shareRecords() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        final String recordsPath = mRecordsFragment.getActivity().getFilesDir().getAbsolutePath() + "/";
        Iterator<Integer> it = mItems.iterator();

        while (it.hasNext()) {
            String name = (String) mRecordsFragment.getListAdapter().getItem(it.next());
            uris.add(Uri.fromFile(new File(recordsPath + name)));
        }

        shareIntent.setType("audio/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(shareIntent, "Share Nullpoint"));

        return true;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
        int n = mRecordsFragment.getListView().getCheckedItemCount();
        actionMode.setTitle(R.string.select_items);

        if (n == 1)
            actionMode.setSubtitle("1 " + getString(R.string.file_selected));
        else
            actionMode.setSubtitle("" + n + " " + getString(R.string.files_selected));

        if (b)
            mItems.add(i);
        else
            mItems.remove(i);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = actionMode.getMenuInflater();
        menuInflater.inflate(R.menu.multiple_select_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
        case R.id.action_delete:
            deleteRecords();
            actionMode.finish();
            return true;
        case R.id.action_share:
            return shareRecords();
        default:
            return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
    }

    class DialogListener implements DialogInterface.OnClickListener {
        private MultiChoiceModeListener mMultiChoiceModeListener;

        public DialogListener(MultiChoiceModeListener multiChoiceModeListener) {
            mMultiChoiceModeListener = multiChoiceModeListener;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int iButton) {
            int i = 0;
            Iterator<Integer> it = mItems.iterator();
            String [] paths = new String[mItems.size()];
            final String activityPath = mMultiChoiceModeListener.mContext.getFilesDir().getAbsolutePath() + "/";

            while (it.hasNext())
                paths[i++] = activityPath + mMultiChoiceModeListener.mRecordsFragment.getListAdapter().getItem(it.next());

            mListener.deleteRecords(paths);
            mMultiChoiceModeListener.mRecordsFragment.loadFiles();
        }
    }
}
