package org.pytorch.demo.objectdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_READ_STORAGE = 2;

    private GridView galleryGrid;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // 뒤로 가기 버튼 클릭 리스너 설정
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 돌아감
                finish();
                overridePendingTransition(0, 0);
            }
        });

        // 사용자 프로필 버튼 클릭 리스너 설정
        ImageView userProfile = findViewById(R.id.user_profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(UploadActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });

        galleryGrid = findViewById(R.id.gallery_grid);
        imagePaths = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imagePaths);
        galleryGrid.setAdapter(imageAdapter);

        galleryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 필요한 경우 그리드 항목 클릭 처리
                String selectedImagePath = imagePaths.get(position);

                // 선택한 이미지로 userImg 업데이트
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                ImageView userImg = findViewById(R.id.user_img);
                userImg.setImageBitmap(bitmap);
            }
        });

        if (checkStoragePermission()) {
            loadImagesFromGallery();
        } else {
            requestStoragePermission();
        }

        ImageButton uploadButton = findViewById(R.id.uploadbutton);

        // Set click listener for the upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Toast message when the upload button is clicked
                Toast.makeText(UploadActivity.this, "Upload completed", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView homepage = findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(UploadActivity.this, MyBoardActivity2.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImagesFromGallery();
            } else {
                // 권한이 거부되었을 때 처리
            }
        }
    }

    private void loadImagesFromGallery() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                imagePaths.add(imagePath);
            }
            cursor.close();
        }
        imageAdapter.notifyDataSetChanged();
    }

    // Optional: 필요한 경우 onBackPressed()를 오버라이드

    private String getMostRecentImagePath() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();
            return imagePath;
        }
        return null;
    }

}
