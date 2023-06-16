package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_saved extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_saved);

        // 이곳에 Profile_saved 액티비티의 동작을 추가할 수 있습니다.

        ImageButton upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(Profile_saved.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton home = findViewById(R.id.homepage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(Profile_saved.this, MyBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        TextView shopbutton = findViewById(R.id.shopbtn);
        shopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(Profile_saved.this, Profile_4shop.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton serButton = findViewById(R.id.search);
        serButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(Profile_saved.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
    }
}
