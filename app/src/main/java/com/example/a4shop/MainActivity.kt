package com.example.a4shop

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.a4shop.databinding.ActivityGalleryBinding
import com.example.a4shop.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_FOR_IMAGE_CAPTURE = 100
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PictureCallback {
    lateinit var binding: ActivityMainBinding
    private lateinit var photoFile: File

    private var camera: Camera? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var showGrid = false
    val GET_GALLERY_IMAGE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gallery버튼 클릭시 Gallery화면으로 전환
        val galleryBtn = findViewById<ImageButton>(R.id.gallery_btn)
        galleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 버튼 클릭시 내장된 갤러리 오픈
        binding.storageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, GET_GALLERY_IMAGE)
        }

        // 카메라 권한이 허용되지 않은 경우 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        // surface holder 초기화, call back 추가
        surfaceHolder = (findViewById<View>(R.id.surfaceView_camera_preview) as SurfaceView).holder
        surfaceHolder!!.addCallback(this)

        // 촬영 버튼 click listener 추가
        binding.buttonTakePicture.setOnClickListener {
            camera?.takePicture(null, null, this)
        }

        // 토글버튼 구현
        binding.guidelineBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.guidelineBtn.isChecked = true
            }
        }

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 카메라 미리보기
        camera = Camera.open()
        try {
            camera?.setPreviewDisplay(holder)
        } catch (e: IOException) {
            Log.e("CameraPreview", "Error setting camera preview display", e)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //  surface 변경되면 카메라 미리보기 중단, 재시작
        camera?.stopPreview()
        camera?.setDisplayOrientation(90)
        camera?.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // surface 사라지면 카메라를 해제
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
        // Do something with the picture data here
        Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show()

        // 촬영 후 카메라 미리보기 재실행
        camera?.startPreview()
    }

    private fun drawGrid() {
        val canvas = surfaceHolder?.lockCanvas() ?: return

        try {
            canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)

            if (showGrid) {
                val width = canvas.width
                val height = canvas.height
                val cellSize = 50
                val paint = Paint().apply {
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeWidth = 2f
                }

                for (x in 0 until width step cellSize) {
                    canvas.drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), paint)
                }

                for (y in 0 until height step cellSize) {
                    canvas.drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), paint)
                }
            }
        } finally {
            surfaceHolder?.unlockCanvasAndPost(canvas)
        }
    }

}
