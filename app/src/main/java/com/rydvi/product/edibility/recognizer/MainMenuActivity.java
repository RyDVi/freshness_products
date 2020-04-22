package com.rydvi.product.edibility.recognizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.rydvi.product.edibility.recognizer.classifier.ClassifierActivity;
import com.rydvi.product.edibility.recognizer.consulting.ProductListActivity;
import com.rydvi.product.edibility.recognizer.guide.GuideListActivity;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_recognizer:
                intent = new Intent(this, ClassifierActivity.class);
                break;
            case R.id.nav_products:
                intent = new Intent(this, ProductListActivity.class);
                break;
            case R.id.nav_guides:
                intent = new Intent(this, GuideListActivity.class);
                break;
            case R.id.nav_exit:
                finish();
                System.exit(0);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        return false;
    }
}
