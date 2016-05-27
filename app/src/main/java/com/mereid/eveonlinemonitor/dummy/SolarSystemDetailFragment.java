package com.mereid.eveonlinemonitor.dummy;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mereid.eveonlinemonitor.R;
import com.mereid.eveonlinemonitor.dummy.dummy.KillContent;

/**
 * A fragment representing a single SolarSystem detail screen.
 * This fragment is either contained in a {@link SolarSystemListActivity}
 * in two-pane mode (on tablets) or a {@link SolarSystemDetailActivity}
 * on handsets.
 */
public class SolarSystemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private KillContent.SolarSystem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SolarSystemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = KillContent.SYSTEM_ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

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
        View rootView = inflater.inflate(R.layout.solarsystem_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            TextView systemIdView = (TextView) rootView.findViewById(R.id.solarsystem_detail);
            systemIdView.setTextColor(Color.WHITE);
            systemIdView.setText(mItem.systemID);

            TextView secView = (TextView) rootView.findViewById(R.id.sec_status);
            secView.setText(mItem.securityStatus);
            secView.setTextColor(Color.WHITE);

            TextView shipKillsView = (TextView) rootView.findViewById(R.id.system_kills);
            shipKillsView.setText(Integer.toString(mItem.shipKills));
            shipKillsView.setTextColor(Color.WHITE);

            TextView podKillView = (TextView) rootView.findViewById(R.id.pod_kills);
            podKillView.setText(Integer.toString(mItem.podKills));
            podKillView.setTextColor(Color.WHITE);

            TextView totalKillsView = (TextView) rootView.findViewById(R.id.total_kills);
            totalKillsView.setText(Integer.toString(mItem.shipKills+mItem.podKills));
            totalKillsView.setTextColor(Color.WHITE);
        }

        return rootView;
    }
}
