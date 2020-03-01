package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_main.*

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
            Toast.makeText(this, "Please input email", Toast.LENGTH_SHORT).show()
            login_emailField.requestFocus()
            return false
        } else if(password.length <= 0) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_SHORT).show()
            login_passwordField.requestFocus()
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
                        Toast.makeText(this, "Authentication success!", Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(this, HomeActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        if(task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Authentication failed : Invalid password!", Toast.LENGTH_SHORT).show()
                        } else if (task.exception is FirebaseAuthInvalidUserException) {
                            Toast.makeText(this, "Authentication failed : Account does not exists!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            // [END sign_in_with_email]
        }
    }
}
