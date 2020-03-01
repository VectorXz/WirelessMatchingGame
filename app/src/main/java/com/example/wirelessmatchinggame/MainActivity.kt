package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        val regisBtn: Button = findViewById<Button>(R.id.regisBtn)
        val loginBtn: Button = findViewById<Button>(R.id.loginBtn)

        regisBtn.setOnClickListener{
            openRegisterView()
        }

        loginBtn.setOnClickListener{
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
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

    fun login(email: String, password: String) {

        if(email.length <= 0 || password.length <= 0) {
            var errorMsg = "Please input";
            if (email.length <= 0) {
                errorMsg = errorMsg + " email"
            }
            if (password.length <= 0) {
                errorMsg = errorMsg + " Password"
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            // [START sign_in_with_email]
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO Login success -> Redirect to game page
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
