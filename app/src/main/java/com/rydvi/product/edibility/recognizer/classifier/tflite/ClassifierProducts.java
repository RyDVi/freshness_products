package com.rydvi.product.edibility.recognizer.classifier.tflite;

import android.app.Activity;

import java.io.IOException;

public class ClassifierProducts extends ClassifierFloatMobileNet {
    public ClassifierProducts(Activity activity, Device device, int numThreads) throws IOException {
        super(activity, device, numThreads);
    }

    @Override
    protected String getModelPath() {
        return "product_recognizer_model.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "product_recognizer_labels.txt";
    }
}
