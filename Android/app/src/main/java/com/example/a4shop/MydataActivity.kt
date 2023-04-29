package com.example.a4shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.a4shop.databinding.ActivityMainBinding
import com.example.a4shop.databinding.ActivityMydataBinding

class MydataActivity : AppCompatActivity() {
    lateinit var binding: ActivityMydataBinding
    private lateinit var mydataAdapter : MydataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMydataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var MDArray = mutableListOf<MDcontents>()
        mydataAdapter = MydataAdapter(MDArray) { content ->
            // content의 데이터를 가지고, 화면에 가이드라인 서비스 적용시키기
        }
        mydataAdapter.notifyDataSetChanged()
        binding.mdRecyclerview.adapter = mydataAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> startActivity(Intent(this, MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}