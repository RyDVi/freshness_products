/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rydvi.product.edibility.recognizer.classifier;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.ProductType;
import com.rydvi.product.edibility.recognizer.classifier.env.BorderedText;
import com.rydvi.product.edibility.recognizer.classifier.env.Logger;
import com.rydvi.product.edibility.recognizer.classifier.tflite.Classifier;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierEdibility;
import com.rydvi.product.edibility.recognizer.classifier.tflite.ClassifierProducts;

import java.io.IOException;
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
    private Map<ProductType.EProductType, ClassifierEdibility> classifierEdibility;
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

        //Создание классификаторов
        try {
            //Создаем классификатор продуктов
            classifierProducts = Classifier.createProductsClassifier(this);
            //Создаем map классификаторов для определения съедобности продуктов.
            //Создаем сразу, чтобы не пересоздавать при определении съедобности.
            classifierEdibility = new LinkedHashMap<>();
            for (ProductType.EProductType productType : ProductType.EProductType.values()) {
                classifierEdibility.put(productType, Classifier.createEdibilityClassifier(this, productType));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (classifierProducts == null) {
            LOGGER.e("No classifier on preview!");
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

                //Определяем, какой классификатор использовать
                List<Classifier.Recognition> resultsClassifierEdibility = null;
                //Определяем, есть ли результат от классификации продуктов
                if (resultsClassifierProducts.size() >= 1) {
                    Classifier.Recognition recognition = resultsClassifierProducts.get(0);
                    if (recognition != null) {
                        //Ищем тип продукта по имени из результата классификации продуктов
                        ProductType.EProductType productType = findProductTypeByName(recognition.getTitle());
                        //Если тип найден, то классифицируем изображение на съедобность
                        if (productType != null) {
                            resultsClassifierEdibility = classifierEdibility.get(productType)
                                    .recognizeImage(rgbFrameBitmap, sensorOrientation);
                        }
                    }
                }

                //Без этого не передаст параметры в функцию (вызывает ошибку)
                final List<Classifier.Recognition> finalResultsClassifierEdibility = resultsClassifierEdibility;
                runOnUiThread(() -> showResultsInBottomSheet(resultsClassifierProducts, finalResultsClassifierEdibility));
            }
            readyForNextImage();
        });
    }

}
