package com.example.wirelessmatchinggame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /* CHECK LOGGED IN YET ? */
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if(user == null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        /* GET CURRENT LOGIN USER EMAIL */

        val currentUser = auth.currentUser?.email
        val userTxt = findViewById<TextView>(R.id.textView10)
        userTxt.text = currentUser.toString()

        /* START GAME BUTTON */

        val startGameBtn: Button = findViewById(R.id.home_startGameBtn)
        startGameBtn.setOnClickListener {
            val selectImageIntent = Intent(this, SelectImageLanding::class.java)
            startActivity(selectImageIntent)
        }

        /* NAVIGATION CODE */

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.menu.getItem(1).isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_account -> {
                    // do this event
                    val accountIntent = Intent(this, ManageAccount::class.java)
                    startActivity(accountIntent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    // do this event
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_stats -> {
                    // do this event
                    val statsIntent = Intent(this, ViewStatistics::class.java)
                    startActivity(statsIntent)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }
}
