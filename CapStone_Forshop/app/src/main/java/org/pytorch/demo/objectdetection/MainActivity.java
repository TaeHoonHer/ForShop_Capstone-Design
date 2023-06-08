package org.pytorch.demo.objectdetection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Runnable {
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private long mLastAnalysisResultTime;

    private SwitchCompat gridSwitch;
    private GridOverlayView gridOverlayView;

    protected HandlerThread mBackgroundThread;
    protected Handler mBackgroundHandler;
    protected Handler mUIHandler;

    private AppCompatButton reverseButton;

    private ImageView mImageView;
    private ResultView mResultView;
    private Button mButtonDetect;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap = null;
    private Module mModule = null;
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;

    private FocusCircleView mFocusCircleView;

    // 기존 MainActivity의 멤버 변수 및 상수
    private GridOverlayView mGridOverlayView;
    private SwitchCompat mGridSwitch;
    // 기존 MainActivity의 멤버 변수 및 상수
    private TextureView mTextureView;

    private boolean isAnalysisEnabled = true;



    static class AnalysisResult {
        private final ArrayList<Result> mResults;

        public AnalysisResult(ArrayList<Result> results) {
            mResults = results;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUIHandler = new Handler(getMainLooper());

        mGridOverlayView = findViewById(R.id.grid_overlay_view);
        mGridSwitch = findViewById(R.id.grid_switch);

        mGridOverlayView.setVisibility(View.INVISIBLE);

        mGridSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGridOverlayView.setVisibility(View.VISIBLE);
                } else {
                    mGridOverlayView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mResultView = findViewById(R.id.resultView);

        startBackgroundThread();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_CODE_CAMERA_PERMISSION);
        } else {
            setupCameraX();
        }

        mFocusCircleView = findViewById(R.id.focus_circle_view);

        // "button_take_picture" 버튼을 찾아 클릭 이벤트 리스너를 설정합니다.
        // 촬영버튼
        Button takePictureButton = findViewById(R.id.button_take_picture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카메라 미리보기 화면을 저장합니다.
                takePicture();
            }
        });

        // "storage_btn" 버튼을 찾아 클릭 이벤트 리스너를 설정합니다.
        AppCompatButton storageButton = findViewById(R.id.storage_btn);
        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // "mydata_btn" 버튼 클릭 리스니
        AppCompatButton myDataButton = findViewById(R.id.mydata_btn);
        myDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "my_page.xml" activity
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        // "gallery_btn" 버튼 클릭 리스니
        ImageButton myGalleryButton = findViewById(R.id.gallery_btn);
        myGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "activity_board.xml" activity
                Intent intent = new Intent(MainActivity.this, MyBoardActivity.class);
                startActivity(intent);
            }
        });

        ImageButton aiButton = findViewById(R.id.ai);
        aiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAnalysis();
            }
        });


    }

    private void toggleAnalysis() {
        isAnalysisEnabled = !isAnalysisEnabled;
        if (isAnalysisEnabled) {
            Toast.makeText(MainActivity.this, "Analyze Image Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Analyze Image Disabled", Toast.LENGTH_SHORT).show();
            hideResults(); // 분석이 비활성화된 경우 결과를 숨깁니다.
        }
    }


    //초점 맞추기
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            // 해당 좌표로 초점을 맞추도록 FocusCircleView에 설정
            mFocusCircleView.setFocusPoint(new PointF(x, y));
        }
        return true;
    }

    private void takePicture() {
        // TextureView에서 현재 화면을 Bitmap으로 캡처합니다.
        Bitmap bitmap = mTextureView.getBitmap();

        // 저장할 파일 경로와 파일 이름을 지정합니다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + "_image.jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/4shop");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = new File(storageDir, fileName);

        // 파일 저장을 위한 OutputStream을 생성합니다.
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            // Bitmap을 JPEG 형식으로 압축하여 파일에 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();

//            // 사진 파일 경로를 MyPageActivity로 전달하기 위해 Intent에 첨부합니다.
//            Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
//            intent.putExtra("photo_path", imageFile.getAbsolutePath());
//            startActivity(intent);

            // 갤러리에 사진을 추가합니다.
            addImageToGallery(imageFile.getAbsolutePath());

            Toast.makeText(MainActivity.this, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "사진 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        } finally {
            // OutputStream을 닫습니다.
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //갤러리에 찍은 사진 추가하기
    private void addImageToGallery(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File imageFile = new File(imagePath);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private static final int REQUEST_IMAGE_PICK = 2;

    //이미지 경로 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // 선택된 이미지 경로를 처리합니다.
            processSelectedImage(picturePath);
        }
    }

    private void processSelectedImage(String imagePath) {
        setContentView(R.layout.image_view_layout);
        // 선택된 이미지를 로드하여 ImageView에 표시하는 코드
        ImageView imageView = findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startBackgroundThread();
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("ModuleActivity");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        stopBackgroundThread();
        super.onDestroy();
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e("Object Detection", "Error on stopping background thread", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setupCameraX();
            } else {
                Toast.makeText(
                        this,
                        "You need to grant CAMERA and WRITE_EXTERNAL_STORAGE permissions to use the object detection feature.",
                        Toast.LENGTH_LONG
                ).show();
                finish();
            }
        }
    }

    private void setupCameraX() {
        mTextureView = getCameraPreviewTextureView();
        final PreviewConfig previewConfig = new PreviewConfig.Builder().build();
        final Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(output -> mTextureView.setSurfaceTexture(output.getSurfaceTexture()));

        final ImageAnalysisConfig imageAnalysisConfig =
                new ImageAnalysisConfig.Builder()
                        .setTargetResolution(new Size(480, 640))
                        .setCallbackHandler(mBackgroundHandler)
                        .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                        .build();
        final ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        imageAnalysis.setAnalyzer((image, rotationDegrees) -> {
            if (SystemClock.elapsedRealtime() - mLastAnalysisResultTime < 10) {
                return;
            }

            final AnalysisResult result = analyzeImage(image, rotationDegrees);
            if (result != null) {
                mLastAnalysisResultTime = SystemClock.elapsedRealtime();
                runOnUiThread(() -> applyToUiAnalyzeImageResult(result));
            }
        });

        CameraX.bindToLifecycle(this, preview, imageAnalysis);
    }

    @WorkerThread
    @Nullable
    private AnalysisResult analyzeImage(ImageProxy image, int rotationDegrees) {
        if (!isAnalysisEnabled) {
            return null;
        }
        try {
            if (mModule == null) {
                mModule = LiteModuleLoader.load(MainActivity.assetFilePath(getApplicationContext(), "best0418.torchscript.ptl"));
                BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("whole.txt")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap bitmap = imgToBitmap(image.getImage());
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegrees);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);

        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
        final Tensor outputTensor = outputTuple[0].toTensor();
        final float[] outputs = outputTensor.getDataAsFloatArray();

        float imgScaleX = (float) bitmap.getWidth() / PrePostProcessor.mInputWidth;
        float imgScaleY = (float) bitmap.getHeight() / PrePostProcessor.mInputHeight;
        float ivScaleX = (float) mResultView.getWidth() / bitmap.getWidth();
        float ivScaleY = (float) mResultView.getHeight() / bitmap.getHeight();

        final ArrayList<Result> results = PrePostProcessor.outputsToNMSPredictions(outputs, imgScaleX, imgScaleY, ivScaleX, ivScaleY, 0, 0);
        return new AnalysisResult(results);
    }

    @UiThread
    protected void applyToUiAnalyzeImageResult(AnalysisResult result) {
        if (isAnalysisEnabled) {
            mResultView.setResults(result.mResults);
            mResultView.invalidate();
            mResultView.setVisibility(View.VISIBLE);
        } else {
            hideResults();
        }
    }

    private Bitmap imgToBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    public void run() {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
        final Tensor outputTensor = outputTuple[0].toTensor();
        final float[] outputs = outputTensor.getDataAsFloatArray();
        final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);

        runOnUiThread(() -> {
            mButtonDetect.setEnabled(true);
            mButtonDetect.setText(getString(R.string.detect));
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            mResultView.setResults(results);
            mResultView.invalidate();
            mResultView.setVisibility(View.VISIBLE);
        });
    }

    @UiThread
    private void hideResults() {
        mResultView.setVisibility(View.INVISIBLE);
    }

    // 기존 MainActivity의 메서드
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
    // 기존 MainActivity의 메서드

    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    protected TextureView getCameraPreviewTextureView() {
        mResultView = findViewById(R.id.resultView);
        return ((ViewStub) findViewById(R.id.object_detection_texture_view_stub))
                .inflate()
                .findViewById(R.id.object_detection_texture_view);
    }
}