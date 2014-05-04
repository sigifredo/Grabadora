package com.eldamar.grabadora.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
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
    private HashSet<Integer> mItemsSelected;

    public RecordsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String [] files = getActivity().fileList();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, files));

        mOnSelection = false;
        mItemsSelected = new HashSet<Integer>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                onListItemLongClick((ListView) parent, v, position, id);
                return true;
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
            int color;

            if (mItemsSelected.contains(position)) {
                color = getResources().getColor(android.R.color.transparent);
                mItemsSelected.remove(position);

                if (mItemsSelected.isEmpty())
                    mOnSelection = false;
            }
            else {
                color = getResources().getColor(R.color.selected);
                mItemsSelected.add(position);
            }

            v.setBackgroundColor(color);
        } else if (mListener != null)
            mListener.onFragmentInteraction(getActivity().getFilesDir().getAbsolutePath() + "/" + l.getItemAtPosition(position).toString());
    }

    public void onListItemLongClick(ListView l, View v, int position, long id) {
        v.setBackgroundColor(getResources().getColor(R.color.selected));
        mItemsSelected.add(position);
        mOnSelection = true;
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
}
