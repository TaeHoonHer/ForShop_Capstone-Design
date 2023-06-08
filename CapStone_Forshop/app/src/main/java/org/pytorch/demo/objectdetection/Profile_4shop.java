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
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Profile_4shop extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_READ_STORAGE = 2;

    private GridView galleryGrid;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_4shop);

        // 사용자 프로필 버튼 클릭 리스너 설정
        ImageView userProfile = findViewById(R.id.user_profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(Profile_4shop.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });

        // 사용자 프로필 버튼 클릭 리스너 설정
        ImageView upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(Profile_4shop.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });

        // 사용자 프로필 버튼 클릭 리스너 설정
        ImageView homepage = findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(Profile_4shop.this, MyBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });

        TextView savedButton = findViewById(R.id.savedbtn);
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(Profile_4shop.this, Profile_saved.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton serButton = findViewById(R.id.search);
        serButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(Profile_4shop.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        TextView personalinfobt = findViewById(R.id.personalinfobtn);
        personalinfobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile" activity
                Intent intent = new Intent(Profile_4shop.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        galleryGrid = findViewById(R.id.fourshop_grid);
        imagePaths = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imagePaths);
        galleryGrid.setAdapter(imageAdapter);

        if (checkStoragePermission()) {
            loadImagesFromGallery();
        } else {
            requestStoragePermission();
        }
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
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/4shop";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }
        });

        if (files != null) {
            for (File file : files) {
                String imagePath = file.getAbsolutePath();
                imagePaths.add(imagePath);
            }
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
