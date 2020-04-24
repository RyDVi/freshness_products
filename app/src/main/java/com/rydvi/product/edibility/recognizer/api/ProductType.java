package com.rydvi.product.edibility.recognizer.api;


import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import java.util.Arrays;
import java.util.List;

public class ProductType extends TypeContainer {
    static final ProductType productType = new ProductType();
    static Type BEEF;
    static Type BREAD;
    static Type ANOTHER;

    public ProductType() {
        BEEF = new Type() {
            @Override
            public String getName() {
                return "bread";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.beef_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };

        BREAD = new Type() {
            @Override
            public String getName() {
                return "bread";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.bread_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        ANOTHER = new Type() {
            @Override
            public String getName() {
                return "another";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getResources().getString(R.string.another_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        listTypes.addAll(Arrays.asList(BEEF, BREAD));
    }

    @Override
    public String getContainerName() {
        return "products";
    }

    @Override
    public List<Type> values() {
        return listTypes;
    }

    public static ProductType getInstance() {
        return productType;
    }

    public static Type getAnother() {
        return ANOTHER;
    }
}






