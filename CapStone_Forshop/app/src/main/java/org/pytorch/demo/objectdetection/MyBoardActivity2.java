package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyBoardActivity2 extends AppCompatActivity {

    private Button likeButton01;
    private Button likeButton;

    private boolean isLiked01 = false;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_2);

        likeButton01 = findViewById(R.id.like_button01);
        likeButton = findViewById(R.id.like_button);

        likeButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked01) {
                    likeButton01.setBackgroundResource(R.drawable.like);
                    Toast.makeText(MyBoardActivity2.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    likeButton01.setBackgroundResource(R.drawable.like_pressed);
                    Toast.makeText(MyBoardActivity2.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                }
                isLiked01 = !isLiked01;
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    likeButton.setBackgroundResource(R.drawable.like);
                    Toast.makeText(MyBoardActivity2.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    likeButton.setBackgroundResource(R.drawable.like_pressed);
                    Toast.makeText(MyBoardActivity2.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                }
                isLiked = !isLiked;
            }
        });

        ImageView profile = findViewById(R.id.user_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProfileActivity 열기
                Intent intent = new Intent(MyBoardActivity2.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 전환 애니메이션 없음
            }
        });

        ImageButton serButton = findViewById(R.id.search);
        serButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(MyBoardActivity2.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
    }
}
