package com.rydvi.product.edibility.recognizer.classifier;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.util.Size;
import android.util.TypedValue;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.ProductType;
import com.rydvi.product.edibility.recognizer.classifier.env.BorderedText;
import com.rydvi.product.edibility.recognizer.classifier.env.Logger;
import com.rydvi.product.edibility.recognizer.classifier.tflite.Classifier;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierFreshness;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierProducts;

import java.io.IOException;
import java.util.List;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
    private Integer sensorOrientation;
    private ClassifierProducts classifierProducts;
    private ClassifierFreshness classifierFreshness;
    private BorderedText borderedText;

    @Override
    protected int getLayoutId() {
        return R.layout.camera_connection_fragment;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }


    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        try {
            classifierProducts = Classifier.createProductsClassifier(this);
            classifierFreshness = Classifier.createFreshnessClassifier(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (classifierProducts == null) {
            LOGGER.e("No products classifier on preview!");
            return;
        }

        if (classifierFreshness == null) {
            LOGGER.e("No freshness classifier on preview!");
            return;
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    }

    @Override
    protected void processImage() {
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        runInBackground(() -> {
            if (classifierProducts != null) {
                final List<Classifier.Recognition> resultsClassifierProducts =
                        classifierProducts.recognizeImage(rgbFrameBitmap, sensorOrientation);
                final List<Classifier.Recognition> resultsClassifierFreshness =
                        classifierFreshness.recognizeImage(rgbFrameBitmap, sensorOrientation);

                //Запись найденного продукта
                if (resultsClassifierProducts != null && resultsClassifierProducts.size() >= 1) {
                    Classifier.Recognition recognitionProduct = resultsClassifierProducts.get(0);
                    if (recognitionProduct != null) {
                        findedProduct = ProductType.getInstance().findTypeByName(recognitionProduct.getTitle());
                    }
                } else {
                    findedProduct = null;
                }

                runOnUiThread(() -> showResultsInWindow(resultsClassifierProducts, resultsClassifierFreshness));
            }
            readyForNextImage();
        });
    }

}
