package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val regisBtn: Button = findViewById<Button>(R.id.regisBtn)
        val loginBtn: Button = findViewById<Button>(R.id.loginBtn)

        regisBtn.setOnClickListener{
            openRegisterView()
        }

        loginBtn.setOnClickListener{
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            login(username, password)
        }
    }

    fun openRegisterView() {
        val regisIntent = Intent(this, CreateAccount::class.java)
        startActivity(regisIntent)
    }

    fun login(username: String, password: String) {

        if(username.length <= 0 || password.length <= 0) {
            var errorMsg = "Please input";
            if (username.length <= 0) {
                errorMsg = errorMsg + " Username"
            }
            if (password.length <= 0) {
                errorMsg = errorMsg + " Password"
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        }
    }
}
