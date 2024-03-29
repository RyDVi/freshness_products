package com.rydvi.product.edibility.recognizer.classifier;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rydvi.product.edibility.recognizer.R;
import com.rydvi.product.edibility.recognizer.api.ProductContainer;
import com.rydvi.product.edibility.recognizer.api.Type;
import com.rydvi.product.edibility.recognizer.classifier.env.ImageUtils;
import com.rydvi.product.edibility.recognizer.classifier.env.Logger;
import com.rydvi.product.edibility.recognizer.classifier.tflite.Classifier.Recognition;
import com.rydvi.product.edibility.recognizer.consulting.ProductDetailActivity;

import java.nio.ByteBuffer;
import java.util.List;

import static com.rydvi.product.edibility.recognizer.api.ProductFreshnessType.EProductFreshnessType;
import static com.rydvi.product.edibility.recognizer.api.ProductFreshnessType.findFreshnessTypeByName;

public abstract class CameraActivity extends AppCompatActivity
        implements OnImageAvailableListener,
        Camera.PreviewCallback,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    private static final Logger LOGGER = new Logger();

    private static final int PERMISSIONS_REQUEST = 1;

    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    protected int previewWidth = 0;
    protected int previewHeight = 0;
    private Handler handler;
    private HandlerThread handlerThread;
    private boolean useCamera2API;
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;
    private LinearLayout layoutEdibilityWindow;
    protected TextView recognitionProductTypeTextView,
            recognitionFreshnessTextView,
            recognitionProductValueTextView,
            recognitionFreshnessValueTextView,
            clickForGoToProductTextView;
    protected Type findedProduct = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LOGGER.d("onCreate " + this);
        super.onCreate(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hasPermission()) {
            setFragment();
        } else {
            requestPermission();
        }

        //Окно с данными о распознавании
        layoutEdibilityWindow = findViewById(R.id.layout_edibility_window);
        recognitionProductTypeTextView = findViewById(R.id.text_product);
        recognitionProductValueTextView = findViewById(R.id.text_product_probability);
        recognitionFreshnessTextView = findViewById(R.id.text_edibility);
        recognitionFreshnessValueTextView = findViewById(R.id.text_edibility_probability);
        clickForGoToProductTextView = findViewById(R.id.text_click_for_go_product);
        clickForGoToProductTextView.setVisibility(TextView.GONE);

        layoutEdibilityWindow.setOnClickListener(view -> {
            //Навигация к продукту, если он был найден.
            //Наденный продукт хранится в переменной findedProduct
            //Навигация не происходит, если найденный продукт - another
            if (findedProduct != null && !findedProduct.equals(ProductContainer.getAnother())) {
                //Необходимо реализовать навигацию к ProductDetailActivity с ИД продукта
                Intent intent = new Intent(this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.ARG_PRODUCT_ID, findedProduct.getName());
                intent.putExtra(ProductDetailActivity.ARG_LAST_ACTIVITY, getClass().getName());
                startActivity(intent);
            }
        });
    }

    protected int[] getRgbBytes() {
        imageConverter.run();
        return rgbBytes;
    }

    /**
     * Callback for android.hardware.Camera API
     */
    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        if (isProcessingFrame) {
            LOGGER.w("Dropping frame!");
            return;
        }

        try {
            // Initialize the storage bitmaps once when the resolution is known.
            if (rgbBytes == null) {
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                previewHeight = previewSize.height;
                previewWidth = previewSize.width;
                rgbBytes = new int[previewWidth * previewHeight];
                onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 90);
            }
        } catch (final Exception e) {
            LOGGER.e(e, "Exception!");
            return;
        }

        isProcessingFrame = true;
        yuvBytes[0] = bytes;
        yRowStride = previewWidth;

        imageConverter =
                () -> ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);

        postInferenceCallback =
                () -> {
                    camera.addCallbackBuffer(bytes);
                    isProcessingFrame = false;
                };
        processImage();
    }

    /**
     * Callback for Camera2 API
     */
    @Override
    public void onImageAvailable(final ImageReader reader) {
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            Trace.beginSection("imageAvailable");
            final Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    () -> ImageUtils.convertYUV420ToARGB8888(
                            yuvBytes[0],
                            yuvBytes[1],
                            yuvBytes[2],
                            previewWidth,
                            previewHeight,
                            yRowStride,
                            uvRowStride,
                            uvPixelStride,
                            rgbBytes);

            postInferenceCallback =
                    () -> {
                        image.close();
                        isProcessingFrame = false;
                    };

            processImage();
        } catch (final Exception e) {
            LOGGER.e(e, "Exception!");
            Trace.endSection();
            return;
        }
        Trace.endSection();
    }

    @Override
    public synchronized void onStart() {
        LOGGER.d("onStart " + this);
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        LOGGER.d("onResume " + this);
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public synchronized void onPause() {
        LOGGER.d("onPause " + this);

        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {
            LOGGER.e(e, "Exception!");
        }

        super.onPause();
    }

    @Override
    public synchronized void onStop() {
        LOGGER.d("onStop " + this);
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        LOGGER.d("onDestroy " + this);
        super.onDestroy();
    }

    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setFragment();
            } else {
                requestPermission();
            }
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(
                        CameraActivity.this,
                        "Camera permission is required for this demo",
                        Toast.LENGTH_LONG)
                        .show();
            }
            requestPermissions(new String[]{PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
        }
    }

    // Returns true if the device supports the required hardware level, or better.
    private boolean isHardwareLevelSupported(
            CameraCharacteristics characteristics, int requiredLevel) {
        int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        // deviceLevel is not LEGACY, can use numerical sort
        return requiredLevel <= deviceLevel;
    }

    private String chooseCamera() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                useCamera2API =
                        (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                                || isHardwareLevelSupported(
                                characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
                LOGGER.i("Camera API lv2?: %s", useCamera2API);
                return cameraId;
            }
        } catch (CameraAccessException e) {
            LOGGER.e(e, "Not allowed to access camera");
        }

        return null;
    }

    protected void setFragment() {
        String cameraId = chooseCamera();

        Fragment fragment;
        if (useCamera2API) {
            CameraConnectionFragment camera2Fragment =
                    CameraConnectionFragment.newInstance(
                            (size, rotation) -> {
                                previewHeight = size.getHeight();
                                previewWidth = size.getWidth();
                                CameraActivity.this.onPreviewSizeChosen(size, rotation);
                            },
                            this,
                            getLayoutId(),
                            getDesiredPreviewFrameSize());

            camera2Fragment.setCamera(cameraId);
            fragment = camera2Fragment;
        } else {
            fragment =
                    new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
        }

        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    protected void readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback.run();
        }
    }

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    @UiThread
    protected void showResultsInWindow(List<Recognition> resultsClassifierProducts,
                                       List<Recognition> resultsClassifierEdibility) {
        if (findedProduct!=null && !findedProduct.equals(ProductContainer.getAnother())) {
            clickForGoToProductTextView.setVisibility(TextView.VISIBLE);
            if (resultsClassifierProducts != null && resultsClassifierProducts.size() >= 1) {
                Recognition recognitionProduct = resultsClassifierProducts.get(0);
                if (recognitionProduct != null) {
                    if (recognitionProduct.getTitle() != null) {
                        recognitionProductTypeTextView.setText((findedProduct != null) ?
                                findedProduct.getTranlatedName(this) : recognitionProduct.getTitle());

                    }
                    if (recognitionProduct.getConfidence() != null)
                        recognitionProductValueTextView.setText(
                                String.format("%.2f", (100 * recognitionProduct.getConfidence())) + "%");
                }
            }
            if (resultsClassifierEdibility != null && resultsClassifierProducts.size() >= 1) {
                Recognition recognitionEdibility = resultsClassifierEdibility.get(0);
                if (recognitionEdibility != null) {
                    if (recognitionEdibility.getTitle() != null) {
                        final EProductFreshnessType freshnessType =
                                findFreshnessTypeByName(recognitionEdibility.getTitle());
                        recognitionFreshnessTextView.setText(freshnessType.getTranlatedName(this));
                        layoutEdibilityWindow.setBackground(freshnessType.getBackgroundDrawable(this));
                        if(!freshnessType.equals(EProductFreshnessType.ANOTHER)){
                            float freshnessProbability = 100 * recognitionEdibility.getConfidence();
                            recognitionFreshnessValueTextView.setText(
                                String.format("%.2f", freshnessProbability) + "%");
                        } else {
                            recognitionFreshnessValueTextView.setText("");
                        }
                    }
                }
            }
        } else {
            //Если продукт был опознан как another (другое, неизвестный продукт)
            recognitionProductTypeTextView.setText(getResources().getString(R.string.product_unknown));
            recognitionProductValueTextView.setText("");
            recognitionFreshnessTextView.setText("");
            recognitionFreshnessValueTextView.setText("");
            layoutEdibilityWindow.setBackground(getResources().getDrawable(R.drawable.rectangle_rounded_unknown, null));
            clickForGoToProductTextView.setVisibility(TextView.GONE);
        }
    }


    /**
     * Запуск классификации изображения и его дальнейшей обработки
     */
    protected abstract void processImage();

    protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

    protected abstract int getLayoutId();

    protected abstract Size getDesiredPreviewFrameSize();


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
