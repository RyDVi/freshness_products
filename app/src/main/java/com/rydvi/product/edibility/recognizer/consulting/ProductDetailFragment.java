package com.rydvi.product.edibility.recognizer.consulting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rydvi.product.edibility.recognizer.R;

import static com.rydvi.product.edibility.recognizer.api.ProductType.EProductType;
import static com.rydvi.product.edibility.recognizer.api.ProductType.findProductTypeByName;


public class ProductDetailFragment extends Fragment {

    public static final String ARG_PRODUCT_ID = "product_id";
    private EProductType detailProduct;
    private TextView textProductConsulting;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        textProductConsulting = rootView.findViewById(R.id.product_detail);

        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        if (getArguments().containsKey(ARG_PRODUCT_ID)) {
            detailProduct = findProductTypeByName(getArguments().getString(ARG_PRODUCT_ID));
            if (appBarLayout != null) {
                appBarLayout.setTitle(detailProduct.getTranlatedName(getContext()));
            }
            textProductConsulting.setText(detailProduct.getConsultingText(getContext()));
        }

        return rootView;
    }
}
