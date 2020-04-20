package com.rydvi.product.edibility.recognizer.api;

import java.util.*;

public final class Localization{
    public static String getJsonPathByLanguage() {
        switch (Locale.getDefault().getDisplayLanguage()) {
            case "ru":
                return JsonLocal.RU.getJsonPath();
            default:
                return JsonLocal.DEFAULT.getJsonPath();
        }
    }
}

enum JsonLocal {
    RU {
        String getJsonPath() {
            return "products_consultings_ru.json";
        }

    },
    DEFAULT {
        String getJsonPath() {
            return "products_consultings_ru.json";
        }

    };
    abstract String getJsonPath();
}


