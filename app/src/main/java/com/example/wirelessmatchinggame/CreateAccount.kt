package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        val regisBackBtn: ImageButton = findViewById(R.id.regisBackBtn)

        regisBackBtn.setOnClickListener{
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }

        val submitBtn : Button = findViewById(R.id.regis_regisBtn)


        submitBtn.setOnClickListener{
            val remail = regis_emailField.text.toString()
            val rpassword = regis_passwordField.text.toString()
            val rconfirmPass = regis_confirmPassField.text.toString()
            //Toast.makeText(this, remail, Toast.LENGTH_SHORT).show()
            registerValidation(remail, rpassword, rconfirmPass)
        }
    }

    fun registerValidation(email: String, password: String, confirmPass: String) {
        if(email.length <= 0) {
            Toast.makeText(this, "Please input email", Toast.LENGTH_SHORT).show()
        } else if (password.length <= 0) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_SHORT).show()
        } else if (confirmPass.length <= 0) {
            Toast.makeText(this, "Please input confirm pass", Toast.LENGTH_SHORT).show()
        } else if (!password.equals(confirmPass)) {
            Toast.makeText(this, "Confirmation password mismatch!", Toast.LENGTH_SHORT).show()
        }
        else {
            createAccount(email, password)
        }


    }

    fun createAccount(email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    //TODO redirect to login page
                } else {
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                }

                // EXCLUDE
                if (!task.isSuccessful) {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
