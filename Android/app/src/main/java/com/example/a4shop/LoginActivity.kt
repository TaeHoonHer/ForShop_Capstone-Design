package com.example.a4shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.a4shop.databinding.ActivityGalleryBinding
import com.example.a4shop.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    private lateinit var etId: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val defaultId = "test_user"
    private val defaultPassword = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        etId = findViewById(R.id.et_id)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)

        binding.btnLogin.setOnClickListener {
            val id = etId.text.toString()
            val password = etPassword.text.toString()

            //로그인 기능 구현
            if (id == defaultId && password == defaultPassword) {
                val intent = Intent(this, GalleryActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 \n올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> startActivity(Intent(this, GalleryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}