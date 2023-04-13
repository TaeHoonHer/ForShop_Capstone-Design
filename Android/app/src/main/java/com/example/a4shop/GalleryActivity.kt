package com.example.a4shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Images
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.a4shop.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var GArray = mutableListOf<Gcontents>(
            Gcontents(R.drawable.mountain),
            Gcontents(R.drawable.bg2),
            Gcontents(R.drawable.bg3),
            Gcontents(R.drawable.bg4),
            Gcontents(R.drawable.bg5),
            Gcontents(R.drawable.bg6),
            Gcontents(R.drawable.bg7),
            Gcontents(R.drawable.bg8),
            Gcontents(R.drawable.bg9),
            Gcontents(R.drawable.bg10),
        )
        val layoutManager = GridLayoutManager(this, 2)
        binding.galleryRecyclerView.layoutManager = layoutManager
        binding.galleryRecyclerView.adapter = GalleryAdapter(GArray) { content ->
            if (content.image === R.drawable.mountain) {
                val intent = Intent(this, BoardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        /*val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            it.data!!.getStringExtra("result")?.let {
                contents?.add(it)
                adapter.notifyDataSetChanged()
            }
        }*/
        binding.addBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> startActivity(Intent(this, MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.gallery, menu)
        return true
    }
}