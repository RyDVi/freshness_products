package com.rydvi.product.edibility.recognizer.api;


import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.rydvi.product.edibility.recognizer.R;

public final class ProductType {
    /**
     * Ищет тим продукта по имени продукта из меток
     *
     * @param name - имя продукта из меток в product_recognier_labels.txt
     * @return - тип продукта
     */
    public static EProductType findProductTypeByName(String name) {
        for (EProductType productType : EProductType.values()) {
            if (name.equalsIgnoreCase(productType.getName())) {
                return productType;
            }
        }
        return null;
    }

    public enum EProductType {
        BEEF {
            @Override
            public String getName() {
                return "beef";
            }


            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.beef_name);
            }

            @Override
            public Spanned getConsultingHtml(Context context) {
                return Html.fromHtml(context.getText(R.string.beef_consulting).toString());
            }

        },
        BREAD {
            @Override
            public String getName() {
                return "bread";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.bread_name);
            }

            @Override
            public Spanned getConsultingHtml(Context context) {
                return null;
            }
        },
        ANOTHER {
            @Override
            public String getName() {
                return "another";
            }

            @Override
            public String getTranlatedName(Context context) {
                return null;
            }

            @Override
            public Spanned getConsultingHtml(Context context) {
                return null;
            }
        };

        public abstract String getName();

        public abstract String getTranlatedName(Context context);

        public abstract Spanned getConsultingHtml(Context context);
    }
}






