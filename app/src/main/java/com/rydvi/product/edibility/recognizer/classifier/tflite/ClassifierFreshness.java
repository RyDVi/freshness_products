package com.rydvi.product.edibility.recognizer.classifier.tflite;

import android.app.Activity;

import com.rydvi.product.edibility.recognizer.api.Product;
import com.rydvi.product.edibility.recognizer.api.ProductType;

import java.io.IOException;

public class ClassifierFreshness extends ClassifierFloatMobileNet {

    protected ClassifierFreshness(Activity activity, Device device, int numThreads) throws IOException {
        super(activity, device, numThreads);
    }

    @Override
    protected String getModelPath() {
        return "fresh_spoiled_graph.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "fresh_spoiled_labels.txt";
    }
}
