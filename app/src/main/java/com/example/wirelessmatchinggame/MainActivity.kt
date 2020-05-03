package com.example.wirelessmatchinggame

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val regisBtn: Button = findViewById<Button>(R.id.login_regisBtn)
        val loginBtn: Button = findViewById<Button>(R.id.login_loginBtn)

        regisBtn.setOnClickListener{
            openRegisterView()
        }

        loginBtn.setOnClickListener{
            val email = login_emailField.text.toString()
            val password = login_passwordField.text.toString()
            login(email, password)
        }

        val langThBtn: ImageButton = findViewById(R.id.langThBtn)
        langThBtn.setOnClickListener {
            Log.d("LANG","TH Btn clicked!")
            setLocate("th")
            recreate()
        }

        val langUsBtn: ImageButton = findViewById(R.id.langUsBtn)
        langUsBtn.setOnClickListener {
            Log.d("LANG","US Btn clicked!")
            setLocate("us")
            recreate()
        }
    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            //TODO Switch to game page
            val homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        }
    }

    fun openRegisterView() {
        val regisIntent = Intent(this, CreateAccount::class.java)
        startActivity(regisIntent)
    }

    fun loginValidation(email: String, password: String): Boolean {
        if(email.length <= 0) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(getString(R.string.loginTxt))
            builder.setMessage(getString(R.string.emailNullAlert))
            builder.setPositiveButton("OK"){dialogInterface, which ->
                login_emailField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return false
        } else if(password.length <= 0) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(getString(R.string.loginTxt))
            builder.setMessage(getString(R.string.passwordNullAlert))
            builder.setPositiveButton("OK"){dialogInterface, which ->
                login_passwordField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            return false
        }
        return true
    }

    fun login(email: String, password: String) {

        if(loginValidation(email, password)) {
            // [START sign_in_with_email]
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO Login success -> Redirect to game page
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle(getString(R.string.loginTxt))
                        builder.setMessage(getString(R.string.loginSuccess))
                        builder.setPositiveButton("OK"){dialogInterface, which ->
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            startActivity(homeIntent)
                        }
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        if(task.exception is FirebaseAuthInvalidCredentialsException) {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(getString(R.string.loginTxt))
                            builder.setMessage(getString(R.string.loginFailedInvalidPass))
                            builder.setPositiveButton("OK"){dialogInterface, which ->
                                login_passwordField.requestFocus()
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.show()
                        } else if (task.exception is FirebaseAuthInvalidUserException) {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(getString(R.string.loginTxt))
                            builder.setMessage(getString(R.string.loginFailedNoAccount))
                            builder.setPositiveButton("OK"){dialogInterface, which ->
                                login_emailField.requestFocus()
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.show()
                        } else {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(getString(R.string.loginTxt))
                            builder.setMessage(getString(R.string.loginFailed))
                            builder.setPositiveButton("OK"){dialogInterface, which ->
                                login_emailField.requestFocus()
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.show()
                        }
                    }
                }
            // [END sign_in_with_email]
        }
    }
}
