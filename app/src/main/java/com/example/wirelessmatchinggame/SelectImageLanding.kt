package com.example.wirelessmatchinggame

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectImageLanding : AppCompatActivity() {

    lateinit var image1: Bitmap
    lateinit var image2: Bitmap
    lateinit var image3: Bitmap
    val maxSize = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image_landing)

        /* TAKE PHOTO BTN CLICK */

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

        /* SELECT IMG FROM GALLERY BTN CLICKED */
        val gallBtn1: ImageButton = findViewById(R.id.gallBtn1)
        gallBtn1.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, 1001);
                }
                else{
                    //permission already granted
                    pickImageFromGallery(1001);
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery(1001);
            }
        }

        val gallBtn2: ImageButton = findViewById(R.id.gallBtn2)
        gallBtn2.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, 1002);
                }
                else{
                    //permission already granted
                    pickImageFromGallery(1002);
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery(1002);
            }
        }

        val gallBtn3: ImageButton = findViewById(R.id.gallBtn3)
        gallBtn3.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, 1003);
                }
                else{
                    //permission already granted
                    pickImageFromGallery(1003);
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery(1003);
            }
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
        var outWidth: Int
        var outHeight: Int
        val inWidth: Int
        val inHeight: Int
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
        } else if (resultCode == Activity.RESULT_OK && requestCode == 1001){
            val imageUri: Uri? = data!!.data
            try {
                imageUri?.let {
                    if(Build.VERSION.SDK_INT < 28) {
                        image1 = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            imageUri
                        )
                        inWidth = image1.width
                        inHeight = image1.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image1 = Bitmap.createScaledBitmap(image1, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb1)
                        imgView.setImageBitmap(image1)
                    } else {
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                        image1 = ImageDecoder.decodeBitmap(source)
                        inWidth = image1.width
                        inHeight = image1.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image1 = Bitmap.createScaledBitmap(image1, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb1)
                        imgView.setImageBitmap(image1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 1002){
            val imageUri: Uri? = data!!.data
            try {
                imageUri?.let {
                    if(Build.VERSION.SDK_INT < 28) {
                        image2 = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            imageUri
                        )
                        inWidth = image2.width
                        inHeight = image2.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image2 = Bitmap.createScaledBitmap(image2, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb2)
                        imgView.setImageBitmap(image2)
                    } else {
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                        image2 = ImageDecoder.decodeBitmap(source)
                        inWidth = image2.width
                        inHeight = image2.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image2 = Bitmap.createScaledBitmap(image2, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb2)
                        imgView.setImageBitmap(image2)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 1003){
            val imageUri: Uri? = data!!.data
            try {
                imageUri?.let {
                    if(Build.VERSION.SDK_INT < 28) {
                        image3 = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            imageUri
                        )
                        inWidth = image3.width
                        inHeight = image3.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image3 = Bitmap.createScaledBitmap(image3, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb3)
                        imgView.setImageBitmap(image3)
                    } else {
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                        image3 = ImageDecoder.decodeBitmap(source)
                        inWidth = image3.width
                        inHeight = image3.height
                        if(inWidth > inHeight){
                            outWidth = maxSize;
                            outHeight = (inHeight * maxSize) / inWidth;
                        } else {
                            outHeight = maxSize;
                            outWidth = (inWidth * maxSize) / inHeight;
                        }
                        image3 = Bitmap.createScaledBitmap(image3, outWidth, outHeight, false)
                        val imgView : ImageView = findViewById(R.id.imgThumb3)
                        imgView.setImageBitmap(image3)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun pickImageFromGallery(number: Int) {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, number)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            requestCode -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery(requestCode)
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
