package com.rydvi.product.edibility.recognizer.consulting;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.Product;
import com.rydvi.product.edibility.recognizer.api.ProductsViewModel;


public class ProductDetailFragment extends Fragment {

    public static final String ARG_PRODUCT_ID = "product_id";
    private ProductsViewModel productsViewModel = null;
    private Product detailProduct;
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

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        productsViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (getArguments().containsKey(ARG_PRODUCT_ID)) {
                int productId = getArguments().getInt(ARG_PRODUCT_ID, 0);
                for (Product product : products) {
                    if (product.getId().equals(productId)) {
                        detailProduct = product;
                        if (appBarLayout != null) {
                            appBarLayout.setTitle(detailProduct.getNameLocal());
                        }
                        textProductConsulting.setText(product.getConsulting());
                        break;
                    }
                }
            }
        });
        productsViewModel.refreshProducts();

        return rootView;
    }
}
