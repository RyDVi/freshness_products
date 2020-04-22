package com.rydvi.product.edibility.recognizer.api;

import android.content.Context;
import android.text.Spanned;

public final class GuideType {
    /**
     * Ищет тим продукта по имени продукта из меток
     *
     * @param name - имя гайда
     * @return - тип гайда
     */
    public static EGuideType findGuideTypeByName(String name) {
        for (EGuideType guideType : EGuideType.values()) {
            if (name.equalsIgnoreCase(guideType.getName())) {
                return guideType;
            }
        }
        return null;
    }

    public enum EGuideType {
        TEST_GUIDE {
            @Override
            public String getName() {
                return "test_guide";
            }

            @Override
            public String getTranlatedName(Context context) {
                return "Testing guide";
            }

            @Override
            public Spanned getContentHtml(Context context) {
                return null;
            }
        };

        public abstract String getName();
        public abstract String getTranlatedName(Context context);
        public abstract Spanned getContentHtml(Context context);
    }
}






