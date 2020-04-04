package com.example.wirelessmatchinggame

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class SelectImageLanding : AppCompatActivity() {

    lateinit var image1: Bitmap
    lateinit var image2: Bitmap
    lateinit var image3: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image_landing)

        val takePhoto1: ImageButton = findViewById(R.id.camBtn1)
        takePhoto1.setOnClickListener {
            dispatchTakePictureIntent(1)
        }

        val takePhoto2: ImageButton = findViewById(R.id.camBtn2)
        takePhoto2.setOnClickListener {
            dispatchTakePictureIntent(2)
        }

        val takePhoto3: ImageButton = findViewById(R.id.camBtn3)
        takePhoto3.setOnClickListener {
            dispatchTakePictureIntent(3)
        }

        val imgStartGameBtn: Button = findViewById(R.id.imgStartGameBtn)
        imgStartGameBtn.setOnClickListener {
            val showIntent = Intent(this, TestShowImage::class.java)
            showIntent.putExtra("Image1", image1)
            showIntent.putExtra("Image2", image2)
            showIntent.putExtra("Image3", image3)
            startActivity(showIntent)
        }
    }

    private fun dispatchTakePictureIntent(order: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, order)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            image1 = data?.extras?.get("data") as Bitmap
            val imgView : ImageView = findViewById(R.id.imgThumb1)
            imgView.setImageBitmap(image1)
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            image2 = data?.extras?.get("data") as Bitmap
            val imgView : ImageView = findViewById(R.id.imgThumb2)
            imgView.setImageBitmap(image2)
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            image3 = data?.extras?.get("data") as Bitmap
            val imgView : ImageView = findViewById(R.id.imgThumb3)
            imgView.setImageBitmap(image3)
        }
    }
}
