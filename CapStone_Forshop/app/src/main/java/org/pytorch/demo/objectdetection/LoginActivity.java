package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {
    EditText edtID, edtPass;
    AppCompatButton LoginBtn;
    TextView Login_TO_Signup, Forgot_Password;

    private static final String SERVER_URL = "http://10.50.210.36:8080/api/auth/login";
    private static final String ACCESS_TOKEN_KEY = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        init();

        // 로그인 상태 확인
        String accessToken = getAccessToken();
        if (accessToken != null) {
            // 이미 로그인된 상태이면 자동으로 ProfileActivity로 이동
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish(); // 현재 로그인 화면 종료
        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = edtID.getText().toString().trim();
                String userPassword = edtPass.getText().toString().trim();

                if (!userId.isEmpty() && !userPassword.isEmpty()) {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(userId, userPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Login_TO_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init() {
        edtID = findViewById(R.id.edt_ID);
        edtPass = findViewById(R.id.edt_pass);

        LoginBtn = findViewById(R.id.btnSignup);
        Login_TO_Signup = findViewById(R.id.login_to_signup);
        Forgot_Password = findViewById(R.id.forgot_password);
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.apply();
    }

    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String userPassword = params[1];

            try {
                URL url = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("userPassword", userPassword);

                String requestBody = jsonObject.toString();

                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    return response.toString();
                } else {
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                Log.d("LoginResponse", response); // 서버 응답 출력

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("accessToken")) {
                        String accessToken = jsonObject.getString("accessToken");
                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        saveAccessToken(accessToken); // accessToken 저장
                        // TODO: 로그인 성공 시 accessToken을 저장하거나 필요한 작업 수행
                        // 자동으로 profile.xml로 이동
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish(); // 현재 로그인 화면 종료
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }
}
