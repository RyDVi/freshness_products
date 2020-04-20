package com.rydvi.product.edibility.recognizer.consulting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.Product;
import com.rydvi.product.edibility.recognizer.api.ProductsViewModel;

import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ProductsViewModel productsViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.product_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        });

        if (findViewById(R.id.product_detail_container) != null) {
            mTwoPane = true;
        }
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        productsViewModel.getProducts().observe(this, products -> {
            if (recyclerView.getAdapter() == null) {
                setupRecyclerView(recyclerView, products);
            } else {
                ((ProductRecyclerAdapter) recyclerView.getAdapter()).refreshProducts(products);
            }
        });
        productsViewModel.refreshProducts();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Product> products) {
        recyclerView.setAdapter(new ProductRecyclerAdapter(this, products));
    }
}
