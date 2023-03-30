package com.example.a4shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.a4shop.databinding.ActivityGalleryBinding
import com.example.a4shop.databinding.ActivityMainBinding

class GalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.listToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /*override fun opOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.home -> startActivity(Intent(this, MainActivity::class.java))
        }
    }*/
}