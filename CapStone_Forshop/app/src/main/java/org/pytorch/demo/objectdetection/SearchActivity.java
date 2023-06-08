package org.pytorch.demo.objectdetection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1;

    private GridView videoGridView;
    private VideoAdapter videoAdapter;
    private ArrayList<String> videoList;

    // AWS S3 credentials
    private static final String ACCESS_KEY = "YOUR_ACCESS_KEY";
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";
    private static final String BUCKET_NAME = "YOUR_BUCKET_NAME";

    private AmazonS3 s3Client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        videoGridView = findViewById(R.id.video_gallery);
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoList);
        videoGridView.setAdapter(videoAdapter);

        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoPath = videoList.get(position);

                if (position == 0) {
                    VideoView videoView1 = findViewById(R.id.video_view1);
                    videoView1.setVideoPath(videoPath);
                    videoView1.start();
                } else if (position == 1) {
                    VideoView videoView2 = findViewById(R.id.video_view2);
                    videoView2.setVideoPath(videoPath);
                    videoView2.start();
                }
            }
        });

        // Read external storage permission request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            loadVideos();
        }

        // Initialize AWS S3 client
        s3Client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
        s3Client.setRegion(Region.getRegion(Regions.DEFAULT_REGION));

        ImageButton uploadButton = findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(SearchActivity.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton user_profile = findViewById(R.id.user_profile);
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton homepage = findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(SearchActivity.this, MyBoardActivity2.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
    }

    private void loadVideos() {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_ADDED};
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"; // Sort by creation timestamp in descending order
        Cursor cursor = getContentResolver().query(videoUri, projection, null, null, sortOrder);

        if (cursor != null) {
            int dataIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            while (cursor.moveToNext()) {
                if (dataIndex != -1) {
                    String videoPath = cursor.getString(dataIndex);
                    videoList.add(videoPath);
                }
            }
            cursor.close();
        }

        // Notify the adapter of data change
        videoAdapter.notifyDataSetChanged();
    }

    private void uploadVideoToS3(String videoPath, String objectKey) {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, new File(videoPath));
        PutObjectResult result = s3Client.putObject(request);

        if (result != null) {
            Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear the videoList to release resources if needed
        videoList.clear();
    }

    public void onSendButtonClick(View view) {
        String videoPath1 = videoList.get(0);
        String objectKey1 = "video1.mp4"; // S3 object key (you can change it as desired)
        uploadVideoToS3(videoPath1, objectKey1);

        String videoPath2 = videoList.get(1);
        String objectKey2 = "video2.mp4"; // S3 object key (you can change it as desired)
        uploadVideoToS3(videoPath2, objectKey2);

        Toast.makeText(this, "Videos uploaded to AWS S3", Toast.LENGTH_SHORT).show();
    }

}
