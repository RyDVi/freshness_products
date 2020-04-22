package com.rydvi.product.edibility.recognizer.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.GuideType;

import java.util.ArrayList;
import java.util.List;

import static com.rydvi.product.edibility.recognizer.api.GuideType.*;


public class GuideListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.guide_detail_container) != null) {
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.guide_list);
        List<EGuideType> guideTypes = new ArrayList<>();
        for (EGuideType guideType : EGuideType.values()) {
            guideTypes.add(guideType);
        }
        setupRecyclerView(guideTypes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(List<EGuideType> guideTypes) {
        recyclerView.setAdapter(new GuideRecyclerAdapter(this, guideTypes, mTwoPane));
    }
}
