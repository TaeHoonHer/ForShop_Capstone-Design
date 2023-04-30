package com.example.a4shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a4shop.databinding.ActivityGalleryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter

private const val FARRAY_PREFS_NAME = "FArrayPrefs"
private const val FARRAY_KEY = "FArrayKey"
class GalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityGalleryBinding
    private lateinit var filterAdapter : FilterAdapter
    var FArray = mutableListOf<Fcontents>()

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

        loadFArray()
        filterAdapter = FilterAdapter(FArray) { content ->
            // 눌렀을때 키워드 관련 이미지들 보이게 하기
        }
        binding.filterRecyclerview.adapter = filterAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.filterPlus.setOnClickListener {
            val dlg = MyDialog(this) { value ->
                FArray.add(Fcontents("#"+value))
                filterAdapter.notifyDataSetChanged()
                saveFArray()
            }

            dlg.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> startActivity(Intent(this, MainActivity::class.java))
            R.id.gallerySearch -> {
                // gallerySearch MenuItem이 선택되었을 때의 동작을 구현
                val searchView = item.actionView as androidx.appcompat.widget.SearchView
                searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // 검색어 제출 시 동작을 구현
                        if (!query.isNullOrEmpty()) {
                            // 검색어가 비어 있지 않은 경우에만 동작을 수행
                            performSearch(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // 검색어 입력 중에 동작을 구현
                        // 필요에 따라 검색어 입력 중에 동작이 필요한 경우 구현
                        return false
                    }
                })
                binding.galleryTitle?.visibility = View.GONE;
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.gallery, menu)

        return true
    }

    private fun performSearch(query: String) {
        // 검색어를 이용한 실제 검색 로직을 구현
        // 검색 결과를 처리하는 부분을 구현
        Toast.makeText(this, "검색어: $query", Toast.LENGTH_SHORT).show()
    }

    // FArray를 저장하는 함수
    private fun saveFArray() {
        val prefs = getSharedPreferences(FARRAY_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(FArray) // Gson을 이용하여 FArray를 JSON 형태로 변환
        editor.putString(FARRAY_KEY, json)
        editor.apply()
    }

    // FArray를 불러오는 함수
    private fun loadFArray() {
        val prefs = getSharedPreferences(FARRAY_PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(FARRAY_KEY, null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<Fcontents>>() {}.type
            FArray = Gson().fromJson(json, type) // JSON을 FArray로 변환
        }
    }
}