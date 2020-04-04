package com.example.wirelessmatchinggame

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_show_image.*

class TestShowImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_show_image)

        val image1 = intent.getParcelableExtra<Bitmap>("Image1")
        val image2 = intent.getParcelableExtra<Bitmap>("Image2")
        val image3 = intent.getParcelableExtra<Bitmap>("Image3")

        imageView2.setImageBitmap(image1)
        imageView4.setImageBitmap(image2)
        imageView5.setImageBitmap(image3)
    }
}
