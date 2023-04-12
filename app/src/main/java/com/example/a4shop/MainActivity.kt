package com.example.a4shop

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Camera
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide.init
import com.example.a4shop.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PictureCallback {
    lateinit var binding: ActivityMainBinding

    private var camera: Camera? = null
    private var cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
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
            takePhotoAndSave(this)
        }

        // 토글버튼 구현
        binding.guidelineBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.guidelineBtn.isChecked = true
            }
        }

        // 카메라 전환 버튼
        binding.reverseBtn.setOnClickListener {
            switchCamera()
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

    // 카메라 후면 전환 메소드
    private fun switchCamera() {
        try {
            // 현재 카메라 정보 가져오기
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, cameraInfo)

            // 현재 카메라의 전/후면 여부 확인
            val isBackCamera = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK

            // 카메라 사용 중지
            camera?.stopPreview()
            camera?.release()
            camera = null

            // 전/후면 카메라 전환
            cameraId = if (isBackCamera) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK

            // 전환된 카메라로 다시 카메라 사용 시작
            camera = Camera.open(cameraId)
            camera?.setDisplayOrientation(90) // 카메라 미리보기 화면 회전 설정
            camera?.setPreviewDisplay(surfaceHolder) // 카메라 미리보기 화면 설정
            camera?.startPreview()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 버튼 클릭 시 사진 촬영 및 MediaStore에 저장
    private fun takePhotoAndSave(context: Context) {

        camera?.takePicture(null, null, { data, camera ->
            // 사진 촬영 후 처리할 동작
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "IMG_$timeStamp.jpg"
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val imageFile = File(storageDir, fileName)

            // 이미지 파일 저장
            try {
                val fos = FileOutputStream(imageFile)
                fos.write(data)
                fos.close()

                // Exif 정보 확인하여 이미지 회전 처리
                val exif = ExifInterface(imageFile.absolutePath)
                val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                val rotationInDegrees = exifToDegrees(rotation)
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                val rotatedBitmap = rotateBitmap(bitmap, rotationInDegrees)
                val outputStream = FileOutputStream(imageFile)
                rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // MediaStore에 이미지 파일 등록
                val contentResolver: ContentResolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                // 사진 찍힌 이미지를 갤러리에 나타내기 위해 스캔
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = imageFile.toUri()
                context.sendBroadcast(mediaScanIntent)

                Toast.makeText(context, "사진이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("TAG", "사진 저장 실패: ${e.message}")
                Toast.makeText(context, "사진 저장 실패", Toast.LENGTH_SHORT).show()
            }

            // 카메라 프리뷰 재개
            camera.startPreview()
        })
    }

    // Exif 회전 정보를 각도로 변환
    private fun exifToDegrees(rotation: Int): Int {
        return when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    // 이미지를 회전하는 함수
    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap? {
        if (degrees == 0) {
            return bitmap
        }

        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 3, bitmap.height.toFloat() / 3)

        return try {
            val rotatedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            rotatedBitmap
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }

}
