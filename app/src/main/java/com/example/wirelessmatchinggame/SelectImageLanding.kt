package com.example.wirelessmatchinggame

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream


/*
REQUEST CODE CHEAT SHEET
X00Y

X=1 = take picture
X=2 = select img from gallery
Y = order
 */

class SelectImageLanding : AppCompatActivity() {

    lateinit var image1: Bitmap
    lateinit var image2: Bitmap
    lateinit var image3: Bitmap
    lateinit var imgPath1: Uri
    lateinit var imgPath2: Uri
    lateinit var imgPath3: Uri
    val maxSize = 150
    var MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image_landing)

        /* TAKE PHOTO BTN CLICK */

        val takePhoto1: ImageButton = findViewById(R.id.camBtn1)
        takePhoto1.setOnClickListener {
            dispatchTakePictureIntent(1001)
        }

        val takePhoto2: ImageButton = findViewById(R.id.camBtn2)
        takePhoto2.setOnClickListener {
            dispatchTakePictureIntent(1002)
        }

        val takePhoto3: ImageButton = findViewById(R.id.camBtn3)
        takePhoto3.setOnClickListener {
            dispatchTakePictureIntent(1003)
        }

        val imgStartGameBtn: Button = findViewById(R.id.imgStartGameBtn)
        imgStartGameBtn.setOnClickListener {

            if(checkImageUploaded()) {
                val txtImg1 = findViewById<EditText>(R.id.txtImg1)
                val txtImg2 = findViewById<EditText>(R.id.txtImg2)
                val txtImg3 = findViewById<EditText>(R.id.txtImg3)

                if (txtImg1.text.toString() == "") {
                    val builder = AlertDialog.Builder(this@SelectImageLanding)
                    builder.setTitle(getString(R.string.alert))
                    builder.setMessage(getString(R.string.inputLabel1))
                    builder.setPositiveButton("OK") { dialogInterface, which ->
                        txtImg1.requestFocus()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                } else if (txtImg2.text.toString() == "") {
                    val builder = AlertDialog.Builder(this@SelectImageLanding)
                    builder.setTitle(getString(R.string.alert))
                    builder.setMessage(getString(R.string.inputLabel2))
                    builder.setPositiveButton("OK") { dialogInterface, which ->
                        txtImg2.requestFocus()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                } else if (txtImg3.text.toString() == "") {
                    val builder = AlertDialog.Builder(this@SelectImageLanding)
                    builder.setTitle(getString(R.string.alert))
                    builder.setMessage(getString(R.string.inputLabel3))
                    builder.setPositiveButton("OK") { dialogInterface, which ->
                        txtImg1.requestFocus()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                } else if (txtImg1.text.toString() != "" && txtImg2.text.toString() != "" && txtImg3.text.toString() != "") {
                    val showIntent = Intent(this, MatchingGame::class.java)
                    showIntent.putExtra("imgThumb1", imgPath1.toString())
                    showIntent.putExtra("imgThumb2", imgPath2.toString())
                    showIntent.putExtra("imgThumb3", imgPath3.toString())

                    showIntent.putExtra("txtImg1", txtImg1.text.toString())
                    showIntent.putExtra("txtImg2", txtImg2.text.toString())
                    showIntent.putExtra("txtImg3", txtImg3.text.toString())

                    startActivity(showIntent)
                }
            }
        }

        /* SELECT IMG FROM GALLERY BTN CLICKED */
        val gallBtn1: ImageButton = findViewById(R.id.gallBtn1)
        gallBtn1.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 2001)
        }

        val gallBtn2: ImageButton = findViewById(R.id.gallBtn2)
        gallBtn2.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 2002)
        }

        val gallBtn3: ImageButton = findViewById(R.id.gallBtn3)
        gallBtn3.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 2003)
        }
    }

    private fun checkImageUploaded(): Boolean {
        if(!this::imgPath1.isInitialized) {
            val builder = AlertDialog.Builder(this@SelectImageLanding)
            builder.setTitle(getString(R.string.alert))
            builder.setMessage(getString(R.string.uploadImage1))
            builder.setPositiveButton("OK", null)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return false
        } else if (!this::imgPath2.isInitialized) {
            val builder = AlertDialog.Builder(this@SelectImageLanding)
            builder.setTitle(getString(R.string.alert))
            builder.setMessage(getString(R.string.uploadImage2))
            builder.setPositiveButton("OK", null)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return false
        } else if (!this::imgPath3.isInitialized) {
            val builder = AlertDialog.Builder(this@SelectImageLanding)
            builder.setTitle(getString(R.string.alert))
            builder.setMessage(getString(R.string.uploadImage3))
            builder.setPositiveButton("OK", null)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return false
        } else {
            return true
        }
    }

    private fun dispatchTakePictureIntent(requestCode: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(
            Bitmap.CompressFormat.JPEG,
            50,
            bytes
        ) // Used for compression rate of the Image : 100 means no compression
        Log.d("[PERMISSION]", "START")
        if (ContextCompat.checkSelfPermission(this@SelectImageLanding,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@SelectImageLanding,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                Log.d("[PERMISSION]", "REQUEST")
                ActivityCompat.requestPermissions(this@SelectImageLanding,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("[PERMISSION]", "GRANTED")
            val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "xyz", null)
            return Uri.parse(path)
        }
        Log.d("[PERMISSION]", "END")
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "xyz", null)
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.d("[PERMISSION]", "RESULT START")
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("[PERMISSION]", "PERMISSION JUST GRANTED!")
                    finish();
                    startActivity(getIntent());
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e("[PERMISSION]", "Permission denied by user")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                1001 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    image1 = data?.extras?.get("data") as Bitmap
                    val uri = getImageUri(getApplicationContext(), image1)
                    Log.d("[1001]", "UPLOAD SUCCESSFUL!" + uri)
                    val imgThumb1 = findViewById<ImageView>(R.id.imgThumb1)
                    imgThumb1.setImageURI(uri);
                    imgPath1 = uri!!
                }
                1002 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    image2 = data?.extras?.get("data") as Bitmap
                    val uri = getImageUri(getApplicationContext(), image2)
                    Log.d("[1002]", "UPLOAD SUCCESSFUL!" + uri)
                    val imgThumb2 = findViewById<ImageView>(R.id.imgThumb2)
                    imgThumb2.setImageURI(uri);
                    imgPath2 = uri!!
                }
                1003 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    image3 = data?.extras?.get("data") as Bitmap
                    val uri = getImageUri(getApplicationContext(), image3)
                    Log.d("[1003]", "UPLOAD SUCCESSFUL!" + uri)
                    val imgThumb3 = findViewById<ImageView>(R.id.imgThumb3)
                    imgThumb3.setImageURI(uri);
                    imgPath3 = uri!!
                }
                2001 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d("[RESULT]", "1")
                    val imageUri = data.getData();
                    Log.d("[RESULT]", "2")
                    val imgThumb1 = findViewById<ImageView>(R.id.imgThumb1)
                    Log.d("[RESULT]", "3")
                    Glide.with(getApplicationContext()).load(imageUri).into(imgThumb1)
                    //imgThumb1.setImageURI(imageUri);
                    Log.d("[RESULT]", "4")
                    Log.d("[2001]", "UPLOAD SUCCESSFUL!" + imageUri)
                    Log.d("[RESULT]", "5")
                    imgPath1 = imageUri!!
                }
                2002 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d("[RESULT]", "1")
                    val imageUri = data.getData();
                    Log.d("[RESULT]", "2")
                    val imgThumb2 = findViewById<ImageView>(R.id.imgThumb2)
                    Log.d("[RESULT]", "3")
                    Glide.with(getApplicationContext()).load(imageUri).into(imgThumb2)
                    //imgThumb2.setImageURI(imageUri);
                    Log.d("[RESULT]", "4")
                    Log.d("[2002]", "UPLOAD SUCCESSFUL!" + imageUri)
                    Log.d("[RESULT]", "5")
                    imgPath2 = imageUri!!
                }
                2003 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d("[RESULT]", "1")
                    val imageUri = data.getData();
                    Log.d("[RESULT]", "2")
                    val imgThumb3 = findViewById<ImageView>(R.id.imgThumb3)
                    Log.d("[RESULT]", "3")
                    Glide.with(getApplicationContext()).load(imageUri).into(imgThumb3)
                    //imgThumb3.setImageURI(imageUri);
                    Log.d("[RESULT]", "4")
                    Log.d("[2003]", "UPLOAD SUCCESSFUL!" + imageUri)
                    Log.d("[RESULT]", "5")
                    imgPath3 = imageUri!!
                }
                else -> Log.e("Image Upload", "Error on image upload "+resultCode+" "+requestCode)
            }
        }
    }


}
