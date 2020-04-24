package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import java.util.Arrays;
import java.util.List;

public class GuideContainer extends TypeContainer {
    private static final GuideContainer GUIDE_CONTAINER = new GuideContainer();
    private static Type RECOGNIZER_GUIDE;
    private static Type PRODUCTS_GUIDE;
    private static Type ABOUT_APPLICATION_GUIDE;

    public GuideContainer() {
        RECOGNIZER_GUIDE = new Type() {
            @Override
            public String getName() {
                return "recognizer";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getString(R.string.guide_recognizer_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        PRODUCTS_GUIDE = new Type() {
            @Override
            public String getName() {
                return "products";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getString(R.string.guide_products_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        ABOUT_APPLICATION_GUIDE = new Type() {
            @Override
            public String getName() {
                return "about_application";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getString(R.string.guide_about_application_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        listTypes.addAll(Arrays.asList(RECOGNIZER_GUIDE, PRODUCTS_GUIDE, ABOUT_APPLICATION_GUIDE));
    }

    @Override
    public String getContainerName() {
        return "guides";
    }

    @Override
    public List<Type> values() {
        return listTypes;
    }

    public static GuideContainer getInstance() {
        return GUIDE_CONTAINER;
    }
}

