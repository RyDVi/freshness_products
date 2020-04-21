package com.rydvi.product.edibility.recognizer.classifier;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.util.Size;
import android.util.TypedValue;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.Product;
import com.rydvi.product.edibility.recognizer.api.ProductType;
import com.rydvi.product.edibility.recognizer.api.ProductsViewModel;
import com.rydvi.product.edibility.recognizer.classifier.env.BorderedText;
import com.rydvi.product.edibility.recognizer.classifier.env.Logger;
import com.rydvi.product.edibility.recognizer.classifier.tflite.Classifier;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierEdibility;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierProducts;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.rydvi.product.edibility.recognizer.api.ProductType.findProductTypeByName;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final float TEXT_SIZE_DIP = 10;
    private Bitmap rgbFrameBitmap = null;
    private Integer sensorOrientation;
    private ClassifierProducts classifierProducts;
    private Map<Product, ClassifierEdibility> edibilityClassifiers;
    private ProductsViewModel productsViewModel;
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

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        Activity that = this;
        productsViewModel.getProducts().observe(this, products -> {
            //Чтобы не пересоздавать классификаторы
            if (classifierProducts == null) {
                try {
                    edibilityClassifiers = new HashMap<>();
                    //Создание классификаторов
                    for (Product product : products) {
                        //Создаем map классификаторов для определения съедобности продуктов.
                        //Создаем сразу, чтобы не пересоздавать при определении съедобности.
                        edibilityClassifiers.put(product, Classifier.createEdibilityClassifier(that, product));
                    }
                    //Создаем классификатор продуктов
                    classifierProducts = Classifier.createProductsClassifier(that);
                    if (classifierProducts == null) {
                        LOGGER.e("No classifier on preview!");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        productsViewModel.refreshProducts();

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
                findedProduct = null;
                //Определяем, какой классификатор использовать
                List<Classifier.Recognition> resultsClassifierEdibility = null;
                //Определяем, есть ли результат от классификации продуктов
                if (resultsClassifierProducts.size() >= 1) {
                    Classifier.Recognition recognition = resultsClassifierProducts.get(0);
                    if (recognition != null) {
                        for (Map.Entry<Product, ClassifierEdibility> entryEdibilityClassifier :
                                edibilityClassifiers.entrySet()) {
                            //Ищем классификатор свежести по имени продукта и запускаем распознавание
                            if (entryEdibilityClassifier.getKey().getName()
                                    .equalsIgnoreCase(recognition.getTitle())) {
                                findedProduct = entryEdibilityClassifier.getKey();
                                resultsClassifierEdibility = entryEdibilityClassifier.getValue()
                                        .recognizeImage(rgbFrameBitmap, sensorOrientation);
                                break;
                            }
                        }
                    }
                }
                //Без этого не передаст параметры в функцию (вызывает ошибку)
                final List<Classifier.Recognition> finalResultsClassifierEdibility = resultsClassifierEdibility;
                runOnUiThread(() -> showResultsInWindow(resultsClassifierProducts, finalResultsClassifierEdibility));
            }
            readyForNextImage();
        });
    }

}
