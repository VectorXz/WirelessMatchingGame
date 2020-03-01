package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "System error, please contact admin.", Toast.LENGTH_SHORT).show()
                    }
                    // [END_EXCLUDE]
                }
            // [END sign_in_with_email]
        }
    }
}
