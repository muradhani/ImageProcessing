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
    private var drawEnabeled = false
    private var hipx: Float = 0f
    private var hipy: Float = 0f
    private var kneex: Float = 0f
    private var kneey: Float = 0f
    private var ankx: Float = 0f
    private var anky: Float = 0f
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
        }
        binding.image.drawListener = this
        if (drawEnabeled) {
            binding.image.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Get the current drawable (bitmap) from the ImageView
                        val drawable = binding.image.drawable
                        if (drawable is BitmapDrawable) {
                            val bmp = drawable.bitmap
                            val scales = calcScale()

                            // Update the landmark position based on touch
                            val touchX = event.x
                            val touchY = event.y
                            // Update landmark positions based on touch events
                            // Adjust these lines to fit your logic
                            hipx = touchX
                            hipy = touchY
                            kneex = touchX
                            kneey = touchY
                            ankx = touchX
                            anky = touchY

                            // Clear the canvas and redraw the points and lines
                            val mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true)
                            val canvas = Canvas(mutableBitmap)
                            val paint = Paint().apply {
                                style = Paint.Style.FILL
                                color = Color.RED // Adjust color as needed
                            }
                            drawPoint(canvas, hipx, hipy, scales[0], scales[1], paint)
                            drawPoint(canvas, kneex, kneey, scales[0], scales[1], paint)
                            drawPoint(canvas, ankx, anky, scales[0], scales[1], paint)
                            canvas.drawLine(
                                hipx * scales[0],
                                hipy * scales[1],
                                kneex * scales[0],
                                kneey * scales[1],
                                paint
                            )
                            canvas.drawLine(
                                kneex * scales[0],
                                kneey * scales[1],
                                ankx * scales[0],
                                anky * scales[1],
                                paint
                            )

                            // Set the modified bitmap back to the ImageView
                            binding.image.setImageBitmap(mutableBitmap)

                        }
                    }
                }
                true
            }
        }
    }
    private fun calcScale(): FloatArray {
        // Get the dimensions of the bitmap and ImageView
        val bitmapWidth = binding.image.drawable.intrinsicWidth
        val bitmapHeight = binding.image.drawable.intrinsicHeight
        val imageViewWidth = binding.image.width
        val imageViewHeight = binding.image.height

        // Calculate scale factors
        val scaleX = bitmapWidth.toFloat() / imageViewWidth
        val scaleY = bitmapHeight.toFloat() / imageViewHeight

        return floatArrayOf(scaleX, scaleY)
    }
    private fun drawPoint(canvas: Canvas, x: Float, y: Float, scaleX: Float, scaleY: Float, paint: Paint) {
        // Transform coordinates based on scale factors
        val scaledX = x * scaleX
        val scaledY = y * scaleY
        canvas.drawCircle(scaledX, scaledY, 10f, paint) // Adjust radius as needed
    }

    override fun onDrawRequested(bitmap: Bitmap) {
        binding.image.setImageBitmap(bitmap)
    }
}
