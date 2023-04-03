package com.example.a4shop

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.a4shop.databinding.ActivityGalleryBinding
import com.example.a4shop.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PictureCallback {
    lateinit var binding: ActivityMainBinding

    private var camera: Camera? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var showGrid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gallery버튼 클릭시 Gallery화면으로 전환
        val galleryBtn = findViewById<ImageButton>(R.id.gallery_btn)
        galleryBtn.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        // 카메라 권한이 허용되지 않은 경우 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        // surface holder 초기화, call back 추가
        surfaceHolder = (findViewById<View>(R.id.surfaceView_camera_preview) as SurfaceView).holder
        surfaceHolder!!.addCallback(this)

        // 촬영 버튼 click listener 추가
        val takePictureButton = findViewById<Button>(R.id.button_take_picture)
        takePictureButton.setOnClickListener {
            camera?.takePicture(null, null, this)
        }

        // 토글버튼 구현
        val guideToggleBtn = findViewById<SwitchCompat>(R.id.guideline_btn)
        guideToggleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                guideToggleBtn.isChecked = true
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
