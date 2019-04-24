package com.nychighschools;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nychighschools.model.Schools;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Schools.School mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Schools.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.id_text)).setText(mItem.id);
            ((TextView) rootView.findViewById(R.id.name_text)).setText(mItem.name);
            ((TextView) rootView.findViewById(R.id.location_text)).setText(mItem.location);

            TextView numtesttaker_text = (TextView) rootView.findViewById(R.id.numtesttaker_text);
            TextView criticalreadingavgscore_text = (TextView) rootView.findViewById(R.id.criticalreadingavgscore_text);
            TextView mathavgscore_text = (TextView) rootView.findViewById(R.id.mathavgscore_text);
            TextView writingavgscore_text = (TextView) rootView.findViewById(R.id.writingavgscore_text);

            if (mItem.SAT != null){

                numtesttaker_text.setText("" + mItem.SAT.numSatTestTakers);
                criticalreadingavgscore_text.setText("" + mItem.SAT.criticalReadingAvgScore);
                mathavgscore_text.setText("" + mItem.SAT.mathAvgScore);
                writingavgscore_text.setText("" + mItem.SAT.writingAvgScore);
            }
        }

        return rootView;
    }
}
