package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.rydvi.product.edibility.recognizer.R;

public final class ProductFreshnessType {
    /**
     * Ищет тим свежести продукта по имени продукта из меток
     * @param name - имя свежести продукта из меток в fresh_spoiled_labels.txt
     * @return - тип продукта
     */
    public static EProductFreshnessType findFreshnessTypeByName(String name) {
        for (EProductFreshnessType productType : EProductFreshnessType.values()) {
            if (name.equalsIgnoreCase(productType.getName())) {
                return productType;
            }
        }
        return null;
    }

    public enum EProductFreshnessType {
        FRESH {
            @Override
            public String getName() {
                return "fresh";
            }


            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.product_freshness_fresh);
            }

            @Override
            public Drawable getBackgroundDrawable(Context context) {
                return context.getResources().getDrawable(R.drawable.rectangle_rounded_freshed, null);
            }
        },
        SPOILED {
            @Override
            public String getName() {
                return "spoiled";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.product_freshness_spoiled);
            }

            @Override
            public Drawable getBackgroundDrawable(Context context) {
                return context.getResources().getDrawable(R.drawable.rectangle_rounded_spoiled, null);
            }
        },
        ANOTHER {
            @Override
            public String getName() {
                return "another";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.product_freshness_unknown);
            }

            @Override
            public Drawable getBackgroundDrawable(Context context) {
                return context.getResources().getDrawable(R.drawable.rectangle_rounded_unknown, null);
            }
        };

        public abstract String getName();
        public abstract String getTranlatedName(Context context);
        public abstract Drawable getBackgroundDrawable(Context context);
    }
}