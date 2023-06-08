package org.pytorch.demo.objectdetection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity {
    EditText edtEmail, edtPass, edtCPass, edtId, edtNickname;
    AppCompatButton SignupBtn;
    TextView signupToLogin;

    private static final int MIN_ID_LENGTH = 7;
    private static final int MAX_ID_LENGTH = 25;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final String SERVER_URL = "http://10.50.210.36:8080/api/auth/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        init();

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = edtId.getText().toString().trim();
                String userPassword = edtPass.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String nickname = edtNickname.getText().toString().trim();

                if (validateInput(userId, email, userPassword, userPassword)) {
                    SignupTask signupTask = new SignupTask();
                    signupTask.execute(userId, userPassword, email, nickname);
                }
            }
        });

        signupToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    void init() {
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        edtCPass = findViewById(R.id.edt_cpass);
        edtId = findViewById(R.id.edt_id);
        edtNickname = findViewById(R.id.edt_nickname);
        SignupBtn = findViewById(R.id.btnSignup);
        signupToLogin = findViewById(R.id.signup_to_login);
    }

    boolean validateInput(String userId, String email, String userPassword, String confirmPassword) {
        if (userId.length() < MIN_ID_LENGTH || userId.length() > MAX_ID_LENGTH) {
            Toast.makeText(this, "아이디는 7~25자로 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userPassword.length() < MIN_PASSWORD_LENGTH || userPassword.length() > MAX_PASSWORD_LENGTH) {
            Toast.makeText(this, "비밀번호는 8~30자로 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!userPassword.equals(confirmPassword)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private class SignupTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String userPassword = params[1];
            String email = params[2];
            String nickname = params[3];

            try {
                URL url = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("userPassword", userPassword);
                jsonObject.put("email", email);
                jsonObject.put("nickname", nickname);

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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");
                    if (success) {
                        Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // 서버로부터 회원가입이 성공적으로 완료되었을 때의 처리를 추가할 수 있습니다.
                        // 예를 들어, 다음 화면으로 이동하거나 추가 동작을 수행할 수 있습니다.
                        // startActivity(new Intent(SignupActivity.this, NextActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SignupActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}