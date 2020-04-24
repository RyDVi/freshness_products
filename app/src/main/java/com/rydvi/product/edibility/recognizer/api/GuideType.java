package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import java.util.Arrays;
import java.util.List;

public class GuideType extends TypeContainer {
    static final GuideType guideType = new GuideType();
    static Type TEST_GUIDE;

    public GuideType() {
        TEST_GUIDE = new Type() {
            @Override
            public String getName() {
                return "testing_guide";
            }

            @Override
            public String getTranlatedName(Context context) {
                return context.getString(R.string.guide_testing_name);
            }

            @Override
            public String getHtmlPath() {
                return getTranslateHtmlToType(this);
            }
        };
        listTypes.addAll(Arrays.asList(TEST_GUIDE));
    }

    @Override
    public String getContainerName() {
        return "guides";
    }

    @Override
    public List<Type> values() {
        return listTypes;
    }

    public static GuideType getInstance() {
        return guideType;
    }
}

