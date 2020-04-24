package com.rydvi.product.edibility.recognizer.consulting;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.Type;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private Activity parentActivity;
    private List<Type> products;

    public ProductRecyclerAdapter(Activity parentActivity, List<Type> products) {
        this.parentActivity = parentActivity;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Type product = products.get(position);
        holder.mProductNameView.setText(product.getTranlatedName(parentActivity));

        holder.itemView.setTag(product);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onClick(View view) {
        Type product = (Type) view.getTag();
        Intent intent = new Intent(parentActivity, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.ARG_PRODUCT_ID, product.getName());
        parentActivity.startActivity(intent);
    }

    void refreshProducts(List<Type> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mProductNameView;

        public ViewHolder(View view) {
            super(view);
            mProductNameView = view.findViewById(R.id.text_product_name);
        }
    }
}
