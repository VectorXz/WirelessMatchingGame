package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ManageAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if(user == null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        val accountBtn: Button = findViewById<Button>(R.id.nav_account)

        accountBtn.setOnClickListener {
            val accountIntent = Intent(this, CreateAccount::class.java)
            startActivity(accountIntent)
        }

        val statsBtn: Button = findViewById<Button>(R.id.nav_stats)

        accountBtn.setOnClickListener {
            val statsIntent = Intent(this, ViewStatistics::class.java)
            startActivity(statsIntent)
        }

    }
}
