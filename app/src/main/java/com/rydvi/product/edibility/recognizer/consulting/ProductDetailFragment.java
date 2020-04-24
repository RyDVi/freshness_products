package com.rydvi.product.edibility.recognizer.consulting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.ProductContainer;
import com.rydvi.product.edibility.recognizer.api.Type;


public class ProductDetailFragment extends Fragment {

    public static final String ARG_PRODUCT_ID = "product_id";
    private Type mDetailProduct;
    private WebView mDetailProductView;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        if (getArguments().containsKey(ARG_PRODUCT_ID)) {
            mDetailProduct = ProductContainer.getInstance().findTypeByName(getArguments().getString(ARG_PRODUCT_ID));
            if (appBarLayout != null) {
                appBarLayout.setTitle(mDetailProduct.getTranlatedName(getContext()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        mDetailProductView = rootView.findViewById(R.id.product_detail);
        if (mDetailProduct != null) {
            mDetailProductView.getSettings().setJavaScriptEnabled(true);
            mDetailProductView.loadUrl(mDetailProduct.getHtmlPath());
        }
        return rootView;
    }
}
