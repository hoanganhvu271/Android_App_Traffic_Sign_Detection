package com.hav.group3.Activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hav.group3.Yolo.Constants.LABELS_PATH
import com.hav.group3.Yolo.Constants.MODEL_PATH
import com.hav.group3.Yolo.BoundingBox
import com.hav.group3.Yolo.Detector
import com.hav.group3.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.Toast
import com.hav.group3.Api.G3Api
import com.hav.group3.Helper.DatabaseHelper
import com.hav.group3.Model.DataResponse
import com.hav.group3.Model.TrafficSign
import com.hav.group3.R
import java.util.LinkedList
import java.util.Queue
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetectActivity : AppCompatActivity(), Detector.DetectorListener {
    private lateinit var binding: ActivityMainBinding
    private val isFrontCamera = false
    private val isDetecting: Array<Boolean> = Array(68) { false }
    val processedDetections = HashSet<Int>()
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var detector: Detector
    val audioQueue: Queue<Int> = LinkedList()
    var mediaPlayer: MediaPlayer? = null
    var db: DatabaseHelper? = null
    private val BASE_URL = "https://backend-1-rnqj.onrender.com"

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var cameraExecutor: ExecutorService

    val audioResourceMap = mapOf(
        "p124a" to R.raw.p124a,
        "w208" to R.raw.w208,
        "w209" to R.raw.w209,
        "w210" to R.raw.w210,
        "w219" to R.raw.w219,
        "w221b" to R.raw.w221b,
        "p102" to R.raw.p102
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db  = DatabaseHelper(this)
        detector = Detector(baseContext, MODEL_PATH, LABELS_PATH, this)
        detector.setup()

        val backButton = findViewById<ImageView>(R.id.idDetectBack);

        backButton.setOnClickListener(){
            finish()
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val rotation = binding.viewFinder.display.rotation

        val cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()

        imageAnalyzer?.setAnalyzer(cameraExecutor) { imageProxy ->
            val bitmapBuffer =
                Bitmap.createBitmap(
                    imageProxy.width,
                    imageProxy.height,
                    Bitmap.Config.ARGB_8888
                )
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
            imageProxy.close()

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

                if (isFrontCamera) {
                    postScale(
                        -1f,
                        1f,
                        imageProxy.width.toFloat(),
                        imageProxy.height.toFloat()
                    )
                }
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height,
                matrix, true
            )

            detector.detect(rotatedBitmap)
        }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.CAMERA] == true) {
            startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.clear()
        cameraExecutor.shutdown()
        job.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    companion object {
        private const val TAG = "Camera"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    override fun onEmptyDetect() {
//        Log.d("Box", "Empty")

        for(i in 0 until 68) {
            isDetecting[i] = false
        }
        binding.overlay.apply {
            setResults(emptyList())
            invalidate()
        }
    }


// ...

    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        scope.launch {
            val isExisted: MutableMap<Int, Int> = mutableMapOf()
            for (i in boundingBoxes.indices) {
                val clsIndex = boundingBoxes[i].cls
                isExisted[clsIndex] = 1
                val sign = db?.getTrafficSign(boundingBoxes[i].clsName)
                if (sign != null) {
                    insertHistory(sign)
                }
                if (!isDetecting[clsIndex]) {
                    val audioName = boundingBoxes[i].clsName.replace(".", "").lowercase()
//                    Log.d("Vu", "hehe")
                    val resourceId = audioResourceMap[audioName]

                    if (resourceId != null) {
                        audioQueue.offer(resourceId)
                        isDetecting[clsIndex] = true
                        processedDetections.add(clsIndex)
                    } else {
                        Log.e("AudioError", "Audio resource not found for name: $audioName")
                    }
                }
            }

            for(i in 0 until 68) {
                if(isExisted[i] != 1 ) {

                    isDetecting[i] = false
//                    Log.d("Vu", isDetecting[1].toString())
                }
            }

            playNextAudio()

            withContext(Dispatchers.Main) {
                binding.inferenceTime.text = "${inferenceTime}ms"
                binding.overlay.apply {
                    setResults(boundingBoxes)
                    invalidate()
                }
            }
        }
    }
    private fun playNextAudio() {
        scope.launch(Dispatchers.IO) {
            val nextAudio = audioQueue.poll()
            if (nextAudio != null) {
                mediaPlayer = MediaPlayer.create(this@DetectActivity, nextAudio)
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                    if (!audioQueue.isEmpty()) {
                        playNextAudio()
                    }
                }
                mediaPlayer?.start()
            }
        }
    }

    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(G3Api::class.java)

    private fun insertHistory(sign: TrafficSign) {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())
        api.createNewHistory(sign.getId(), currentDateandTime, "B21DCCN343")
            ?.enqueue( object : Callback<DataResponse?> {
                override fun onResponse(call: Call<DataResponse?>, response: Response<DataResponse?>) {
                    if (response.isSuccessful && response.body()?.status == 200){
                        Toast.makeText(this@DetectActivity, "Saved!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this@DetectActivity, "Not Saved!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DataResponse?>, t: Throwable) {
//                    Toast.makeText(this@DetectActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            })

    }

}
