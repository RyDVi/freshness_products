package com.rydvi.product.edibility.recognizer.consulting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.classifier.ClassifierActivity;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String ARG_PRODUCT_ID = "product_id";
    //Параметр необходим для навигации в ClassifierActivity, поскольку с помощью навигации назад
    //камера перестает работать
    public static final String ARG_LAST_ACTIVITY = "activity_class_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ProductDetailFragment.ARG_PRODUCT_ID,
                    getIntent().getStringExtra(ARG_PRODUCT_ID));
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String lastActivity = getIntent().getStringExtra(ARG_LAST_ACTIVITY);
        if (lastActivity == null) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                navigateUpTo(new Intent(this, ProductListActivity.class));
                return true;
            }
        } else if (lastActivity.equalsIgnoreCase(ClassifierActivity.class.getName())) {
            Intent intent = new Intent(this, ClassifierActivity.class);
            startActivity(intent);
            return true;
        } else {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                navigateUpTo(new Intent(this, ProductListActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
