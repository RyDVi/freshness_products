package com.rydvi.product.edibility.recognizer.api;


import static com.rydvi.product.edibility.recognizer.api.ProductType.EProductType.*;

public final class ProductType {
    public static EProductType findProductTypeByName(String name) {
        if (name == BEEF.getName()) {
            return BEEF;
        } else if (name == BREAD.getName()) {
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
                return null;
            }

            @Override
            public String getLabelsPath() {
                return null;
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
        };

        public abstract String getName();

        public abstract String getModelPath();

        public abstract String getLabelsPath();
    }
}






