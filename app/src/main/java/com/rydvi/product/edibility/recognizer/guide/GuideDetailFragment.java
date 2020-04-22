package com.rydvi.product.edibility.recognizer.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.GuideType;

import static com.rydvi.product.edibility.recognizer.api.GuideType.EGuideType;

public class GuideDetailFragment extends Fragment {
    public static final String ARG_GUIDE_ID = "guide_id";
    private EGuideType mGuideDetail;

    public GuideDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_GUIDE_ID)) {
            Activity activity = this.getActivity();
            String guideName = getArguments().getString(ARG_GUIDE_ID);
            mGuideDetail = GuideType.findGuideTypeByName(guideName);
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mGuideDetail.getTranlatedName(getContext()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.guide_detail, container, false);

        if (mGuideDetail != null) {
            ((TextView) rootView.findViewById(R.id.guide_detail)).setText(mGuideDetail.getTranlatedName(getContext()));
        }

        return rootView;
    }
}
