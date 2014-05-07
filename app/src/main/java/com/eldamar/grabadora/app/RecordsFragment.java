package com.eldamar.grabadora.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class RecordsFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private boolean mOnSelection;
    private HashSet<View> mItemsSelected;
    protected Object mActionMode;

    public RecordsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String [] files = getActivity().fileList();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, files));

        mOnSelection = false;
        mItemsSelected = new HashSet<View>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                if (mActionMode != null) {
                    return false;
                } else {
                    mActionMode = getActivity()
                            .startActionMode(mActionModeCallback);
                    v.setSelected(true);
                    onListItemLongClick((ListView) parent, v, position, id);
                    return true;
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
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

        if (mOnSelection)
        {
            if (mItemsSelected.contains(v)) {
                v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mItemsSelected.remove(v);

                if (mItemsSelected.isEmpty())
                    deactivateMultipleSelection();
            } else {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                mItemsSelected.add(v);
            }
        } else if (mListener != null)
            mListener.onFragmentInteraction(getActivity().getFilesDir().getAbsolutePath() + "/" + l.getItemAtPosition(position).toString());
    }

    public void onListItemLongClick(ListView l, View v, int position, long id) {
        v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        mItemsSelected.add(v);
        mOnSelection = true;
    }

    public void deactivateMultipleSelection() {
        mOnSelection = false;

        if (!mItemsSelected.isEmpty()) {
            Iterator<View> it = mItemsSelected.iterator();
            while (it.hasNext())
                it.next().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }
    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // called when the action mode is created; startActionMode() was called
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            // assumes that you have "contexual.xml" menu resources
            inflater.inflate(R.menu.multiple_select_bar, menu);
            return true;
        }

        // the following method is called each time
        // the action mode is shown. Always called after
        // onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // called when the user selects a contextual menu item
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            /*
            switch (item.getItemId()) {
                case R.id.menuitem1_show:
                    show();
                    // the Action was executed, close the CAB
                    mode.finish();
                    return true;
                default:
                    return false;
            }
            */
            return true;
        }

        // called when the user exits the action mode
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            deactivateMultipleSelection();
        }
    };
}
