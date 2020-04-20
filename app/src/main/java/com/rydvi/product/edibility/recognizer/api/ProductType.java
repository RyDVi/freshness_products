package com.rydvi.product.edibility.recognizer.api;


import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import static com.rydvi.product.edibility.recognizer.api.ProductType.EProductType.*;

public final class ProductType {
    public static EProductType findProductTypeByName(String name) {
        if (name.equalsIgnoreCase(BEEF.getName())) {
            return BEEF;
        } else if (name.equalsIgnoreCase(BREAD.getName())) {
            return BREAD;
        } else {
            return null;
        }
    }

    public enum EProductType {
        BEEF {
            @Override
            public String getName() {
                return "beef";
            }

            @Override
            public String getModelPath() {
                return "mobilenet_v1_1.0_224.tflite";//Временно
            }

            @Override
            public String getLabelsPath() {
                return "labels.txt";//Временно
            }

            @Override
            public String getTranlatedName(Context context ) {
                return context.getResources().getString(R.string.beef_name);
            }

        },
        BREAD {
            @Override
            public String getName() {
                return "bread";
            }

            @Override
            public String getModelPath() {
                return "bread_fresh_spoiled.tflite";
            }

            @Override
            public String getLabelsPath() {
                return "bread_fresh_spoiled.txt";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.bread_name);
            }
        };

        public abstract String getName();

        public abstract String getModelPath();

        public abstract String getLabelsPath();

        public abstract String getTranlatedName(Context context);
    }
}






