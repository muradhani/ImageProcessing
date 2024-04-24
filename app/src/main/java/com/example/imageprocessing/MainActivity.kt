package com.example.imageprocessing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.example.imageprocessing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , ZoomingImage.OnDrawListener{
    lateinit var binding: ActivityMainBinding
    val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.image.setImageURI(uri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.openGalleryBtn.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.enableDrawBtn.setOnClickListener {
           binding.image.drawEnabled = !binding.image.drawEnabled
            Toast.makeText(this,"draw "+binding.image.drawEnabled,Toast.LENGTH_SHORT).show()
        }
        binding.image.drawListener = this
    }
    override fun onDrawRequested(bitmap: Bitmap) {
        binding.image.setImageBitmap(bitmap)
    }
}
