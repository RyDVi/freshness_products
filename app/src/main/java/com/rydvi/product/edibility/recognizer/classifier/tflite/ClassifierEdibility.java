package com.rydvi.product.edibility.recognizer.classifier.tflite;

import android.app.Activity;

import com.rydvi.product.edibility.recognizer.api.Product;
import com.rydvi.product.edibility.recognizer.api.ProductType;

import java.io.IOException;

public class ClassifierEdibility extends ClassifierFloatMobileNet {

    protected ClassifierEdibility(Activity activity, Device device, int numThreads, Product product) throws IOException {
        super(activity, device, numThreads, product);
    }

    @Override
    protected String getModelPath() {
        return product.getModelFilename();
    }

    @Override
    protected String getLabelPath() {
        return product.getLabelsFilename();
    }
}
