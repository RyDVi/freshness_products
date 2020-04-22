package com.rydvi.product.edibility.recognizer.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rydvi.product.edibility.recognizer.R;

import java.util.List;

import static com.rydvi.product.edibility.recognizer.api.GuideType.EGuideType;

public class GuideRecyclerAdapter extends RecyclerView.Adapter<GuideRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private AppCompatActivity mParentActivity;
    private List<EGuideType> mGuides;
    private boolean mTwoPane;

    public GuideRecyclerAdapter(AppCompatActivity parentActivity, List<EGuideType> guides, boolean twoPane) {
        this.mParentActivity = parentActivity;
        this.mGuides = guides;
        this.mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guide_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EGuideType guide = mGuides.get(position);
        holder.mGuideNameView.setText(guide.getTranlatedName(mParentActivity));

        holder.itemView.setTag(guide);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mGuides.size();
    }

    @Override
    public void onClick(View view) {
        EGuideType guide = (EGuideType) view.getTag();
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(GuideDetailFragment.ARG_GUIDE_ID, guide.getName());
            GuideDetailFragment fragment = new GuideDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.guide_detail_container, fragment)
                    .commit();
        } else {
            Context context = view.getContext();
            Intent intent = new Intent(context, GuideDetailActivity.class);
            intent.putExtra(GuideDetailFragment.ARG_GUIDE_ID, guide.getName());

            context.startActivity(intent);
        }
    }

    void refreshGuides(List<EGuideType> guides) {
        this.mGuides = guides;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mGuideNameView;

        public ViewHolder(View view) {
            super(view);
            mGuideNameView = view.findViewById(R.id.text_guide_name);
        }
    }
}
