package com.rydvi.product.edibility.recognizer.consulting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.rydvi.product.edibility.recognizer.R;

import java.util.ArrayList;
import java.util.List;

import static com.rydvi.product.edibility.recognizer.api.ProductType.EProductType;


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
        List<EProductType> productTypes = new ArrayList<>();
        for (EProductType productType : EProductType.values()) {
            if (!productType.equals(EProductType.ANOTHER)) {
                productTypes.add(productType);
            }
        }
        if (recyclerView.getAdapter() == null) {
            setupRecyclerView(recyclerView, productTypes);
        } else {
            ((ProductRecyclerAdapter) recyclerView.getAdapter()).refreshProducts(productTypes);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<EProductType> products) {
        recyclerView.setAdapter(new ProductRecyclerAdapter(this, products));
    }
}
