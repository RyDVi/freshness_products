package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;

import com.rydvi.product.edibility.recognizer.R;

import java.util.ArrayList;
import java.util.List;

public class GuideType extends TypeContainer{
    static final GuideType guideType = new GuideType();
    static final Type TEST_GUIDE = new Type() {
        @Override
        public String getName() {
            return "test_guide";
        }

        @Override
        public String getTranlatedName(Context context) {
            return context.getString(R.string.guide_testing_name);
        }

        @Override
        public String getHtmlPath() {
            return "file:///android_asset/pages/guides/testing_guide.html";
        }
    };

    @Override
    public List<Type> values() {
        List guideTypes = new ArrayList<Type>();
        guideTypes.add(TEST_GUIDE);
        return guideTypes;
    }

    public static GuideType getInstance() {
        return guideType;
    }
    public static Type getTestGuide(){
        return TEST_GUIDE;
    }
}

