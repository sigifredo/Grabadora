package com.caguapp.grabadora.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Iterator;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class RecordsFragment extends ListFragment {

    private InteractionListener mListener;

    public RecordsFragment() {
    }

    public void loadFiles() {
        String [] files = getActivity().fileList();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, files));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFiles();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            HashSet<Integer> mItems = new HashSet<Integer>();

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int n = getListView().getCheckedItemCount();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("¿Está seguro de querer eliminar las grabaciones?")
                                .setTitle("Eliminar archivos")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int iButton) {
                                        int i = 0;
                                        Iterator<Integer> it = mItems.iterator();
                                        String [] paths = new String[mItems.size()];
                                        final String activityPath = getActivity().getFilesDir().getAbsolutePath() + "/";

                                        while (it.hasNext())
                                            paths[i++] = activityPath + getListAdapter().getItem(it.next());

                                        mListener.deleteRecords(paths);
                                        loadFiles();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .create().show();
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (InteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.play(getActivity().getFilesDir().getAbsolutePath() + "/" + l.getItemAtPosition(position).toString());
    }

    public interface InteractionListener {
        public void play(String path);
        public void deleteRecords(String [] paths);
    }
}
