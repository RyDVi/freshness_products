package com.rydvi.product.edibility.recognizer.classifier.tflite;

import android.app.Activity;

import com.rydvi.product.edibility.recognizer.api.ProductType;

import java.io.IOException;

public class ClassifierEdibility extends ClassifierFloatMobileNet {

    protected ClassifierEdibility(Activity activity, Device device, int numThreads, ProductType.EProductType productType) throws IOException {
        super(activity, device, numThreads, productType);
    }

    @Override
    protected String getModelPath() {
        return productType.getModelPath();
    }

    @Override
    protected String getLabelPath() {
        return productType.getLabelsPath();
    }
}
