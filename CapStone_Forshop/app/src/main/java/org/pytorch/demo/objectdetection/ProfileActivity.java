package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN_KEY = "access_token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        ImageButton upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton home = findViewById(R.id.homepage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(ProfileActivity.this, MyBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        TextView savedButton = findViewById(R.id.savedbtn);
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(ProfileActivity.this, Profile_saved.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        TextView profilebtn = findViewById(R.id.profilebtn);
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(ProfileActivity.this, Profile_4shop.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action
                // Clear access token from shared preferences or perform any other necessary logout operations
                // For example:
                clearAccessToken();

                // Open LoginActivity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        ImageButton serButton = findViewById(R.id.search);
        serButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the "Profile_saved" activity
                Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
    }
    private void clearAccessToken() {
        // Clear the access token from shared preferences or any other storage mechanism
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.apply();
    }

}
