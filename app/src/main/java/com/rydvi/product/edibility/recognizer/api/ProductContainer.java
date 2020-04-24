package com.rydvi.product.edibility.recognizer.api;


import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import java.util.Arrays;
import java.util.List;

public class ProductContainer extends TypeContainer {
    private static final ProductContainer PRODUCT_CONTAINER = new ProductContainer();
    private static Type BEEF;
    private static Type BREAD;
    private static Type ANOTHER;

    public ProductContainer() {
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

    public static ProductContainer getInstance() {
        return PRODUCT_CONTAINER;
    }

    public static Type getAnother() {
        return ANOTHER;
    }
}






