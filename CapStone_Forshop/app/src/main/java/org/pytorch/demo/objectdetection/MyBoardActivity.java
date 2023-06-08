package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MyBoardActivity extends AppCompatActivity {

    private ImageView userImage;
    private Button[] likeButtons;
    private boolean[] isLiked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(MyBoardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userImage = findViewById(R.id.user_img);
        likeButtons = new Button[3]; // 좋아요 버튼의 개수에 맞게 배열 크기를 조정합니다.
        isLiked = new boolean[3]; // 좋아요 상태 배열을 초기화합니다.

        likeButtons[0] = findViewById(R.id.like_button);
        likeButtons[1] = findViewById(R.id.like_button2);
        likeButtons[2] = findViewById(R.id.like_button3);
        // 필요한 만큼 좋아요 버튼을 추가로 할당합니다.

        for (int i = 0; i < likeButtons.length; i++) {
            final int index = i; // 인덱스 값을 final 변수로 저장

            likeButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button likeButton = likeButtons[index]; // 현재 클릭된 버튼 가져오기
                    boolean wasLiked = isLiked[index]; // 이전 좋아요 상태를 저장합니다
                    isLiked[index] = !isLiked[index]; // Toggle the liked state

                    if (isLiked[index]) {
                        // Change the image to liked image
                        likeButton.setBackgroundResource(R.drawable.like_pressed);
                    } else {
                        // Change the image to original image
                        likeButton.setBackgroundResource(R.drawable.like);
                    }

                    if (isLiked[index] && !wasLiked) {
                        Toast.makeText(MyBoardActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    } else if (!isLiked[index] && wasLiked) {
                        Toast.makeText(MyBoardActivity.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        ImageView userProfile = findViewById(R.id.user_profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(MyBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });

        ImageButton upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(MyBoardActivity.this, UploadActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
        ImageButton search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ProfileActivity
                Intent intent = new Intent(MyBoardActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // 화면 전환 효과 없음
            }
        });
        // 이미지 로딩 라이브러리 Glide의 옵션 설정
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading) // 이미지 로딩 동안 보여줄 플레이스홀더 이미지
                .error(R.drawable.loading_fail); // 이미지 로딩 실패 시 보여줄 에러 이미지

        // JSON 형식의 이미지를 가져오는 비동기 작업을 수행
        retrieveImageFromServer(requestOptions);
    }

    private void retrieveImageFromServer(RequestOptions requestOptions) {
        // 서버에서 이미지를 가져오는 API 호출

        // API 호출을 위한 Retrofit 객체 생성 및 인터페이스 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.50.210.36:8080/") // 서버의 기본 URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 컨버터 설정
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<JsonObject> call = apiService.getImages(); // 이미지를 가져오는 API 메서드 호출

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body(); // 이미지 데이터를 JsonObject로 받아옴

                    if (jsonResponse != null) {
                        JsonArray jsonItems = jsonResponse.getAsJsonArray("content"); // 이미지 목록을 가져옴
                        createBoardItemsFromJson(jsonItems); // JSON에서 게시판 아이템 생성

                        // Debug: JSON 응답 확인
                        Log.d("MyBoardActivity", "JSON 응답: " + jsonResponse.toString());
                    } else {
                        // API 호출이 실패한 경우
                    }
                } else {
                    // API 호출이 실패한 경우
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // API 호출 실패 또는 예외 발생한 경우
                Log.d("MyBoardActivity", "API 호출 실패 또는 예외 발생: " + t.getMessage());
            }
        });
    }

    private void createBoardItemsFromJson(JsonArray jsonItems) {
        LinearLayout boardLayout = findViewById(R.id.board_layout);

        // JSON에서 가져온 아이템 수만큼 반복하여 레이아웃을 동적으로 생성
        for (int i = 0; i < jsonItems.size(); i++) {
            // board_item.xml의 레이아웃을 인플레이트하여 LinearLayout 객체로 변환
            LinearLayout boardItemLayout = (LinearLayout) getLayoutInflater()
                    .inflate(R.layout.board_item, null);

            // board_item.xml에 있는 뷰들을 findViewById()를 통해 참조
            ImageView boardImage = boardItemLayout.findViewById(R.id.board_image);
            Button likeButton = boardItemLayout.findViewById(R.id.like_button);

            // 이미지 로딩 라이브러리 Glide의 옵션 설정
//            RequestOptions requestOptions = new RequestOptions()
//                    .placeholder(R.drawable.loading) // 이미지 로딩 동안 보여줄 플레이스홀더 이미지
//                    .error(R.drawable.loading_fail); // 이미지 로딩 실패 시 보여줄 에러 이미지

            // JSON에서 이미지 storedName을 가져옴
            JsonObject jsonItem = jsonItems.get(i).getAsJsonObject();
            String storedName = jsonItem.get("storedName").getAsString();
            String imageUrl = "https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + storedName;

            // Debug: 이미지 URL 확인
            Log.d("MyBoardActivity", "이미지 URL: " + imageUrl);


//            // Glide를 사용하여 이미지 로딩
//            if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png")) {
//                // Display image
//                Glide.with(this)
//                        .load(imageUrl)
//                        .apply(requestOptions)
//                        .into(boardImage);
//            } else if (imageUrl.endsWith(".mp4")) {
//                // Display video thumbnail
//                Glide.with(this)
//                        .asBitmap()
//                        .load(imageUrl)
//                        .apply(requestOptions)
//                        .into(boardImage);
//            }

            // Check if the image URL ends with ".jpg"
// Check if the image URL ends with ".jpg"
            if (imageUrl.endsWith(".jpg")) {
                // Glide를 사용하여 이미지 로딩
                Log.d("MyBoardActivity", "이미지 로드를 시도합니다. URL: " + imageUrl);
                Picasso.get()
                        .load(imageUrl)
//        .placeholder(R.drawable.loading) // 로딩 동안 표시할 플레이스홀더 이미지
                        .into(boardImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });

                // board_layout에 동적으로 생성한 board_item.xml의 레이아웃을 추가
                boardLayout.addView(boardItemLayout);
            }


            boardImage.post(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = boardImage.getDrawable();
                    if (drawable != null) {
                        // 이미지가 로드되었음을 확인
                    } else {
                        // 이미지 로드 실패 또는 이미지가 아직 로드되지 않음을 확인
                    }
                }
            });

        }
    }




    private interface ApiService {
        @GET("api/articles") // 이미지를 가져오는 API 경로
        Call<JsonObject> getImages();
    }

    @Override
    public void onBackPressed() {
        // Navigate back to MainActivity
        Intent intent = new Intent(MyBoardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}