package com.rydvi.product.edibility.recognizer.consulting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.ProductContainer;
import com.rydvi.product.edibility.recognizer.api.Type;

import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.product_list);

        if (findViewById(R.id.product_detail_container) != null) {
            mTwoPane = true;
        }
        //Добавляем все типы продуктов, кроме ANOTHER
        if (recyclerView.getAdapter() == null) {
            setupRecyclerView(recyclerView, ProductContainer.getInstance().values());
        } else {
            ((ProductRecyclerAdapter) recyclerView.getAdapter()).refreshProducts(ProductContainer.getInstance().values());
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Type> products) {
        recyclerView.setAdapter(new ProductRecyclerAdapter(this, products));
    }
}
