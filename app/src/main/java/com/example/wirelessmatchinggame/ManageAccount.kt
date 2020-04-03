package com.example.wirelessmatchinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_manage_account.*

class ManageAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)

        /* CHECK LOGGED IN YET ? */
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if(user == null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        /* SHOWING CURRENT LOGGED IN EMAIL ADDRESS */

        val currentUser = auth.currentUser?.email
        val emailField = findViewById<TextView>(R.id.edit_emailField)
        emailField.text = currentUser.toString()

        /* UPDATE BUTTON CODE */
        val updateBtn : Button = findViewById(R.id.edit_updateBtn)


        updateBtn.setOnClickListener{
            val editEmail = edit_emailField.text.toString()
            val editOldPass = edit_passwordField.text.toString()
            val editNewPass = edit_newpassfield.text.toString()
            val editCFNewPass = edit_cfnewpassfield.text.toString()
            //Toast.makeText(this, remail, Toast.LENGTH_SHORT).show()
            updateValidation(editEmail, editOldPass, editNewPass, editCFNewPass)
        }

        /* LOGOUT BTN CODE */

        val logoutBtn : Button = findViewById(R.id.edit_logoutBtn)
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }


        /* NAVIGATION CODE */

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.menu.getItem(0).isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_account -> {
                    // do this event
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    // do this event
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
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

    fun updateValidation(email: String, oldPass: String, newPass: String, cfNewPass: String) {
        if(email.length <= 0) {
            Toast.makeText(this, "Error retrieving email address, please contact administrator!", Toast.LENGTH_SHORT).show()
        } else if (oldPass.length <= 0) {
            Toast.makeText(this, "Please input old password", Toast.LENGTH_SHORT).show()
            edit_passwordField.requestFocus()
        } else if (newPass.length <= 0) {
            Toast.makeText(this, "Please input new password", Toast.LENGTH_SHORT).show()
            edit_newpassfield.requestFocus()
        } else if (cfNewPass.length <= 0) {
            Toast.makeText(this, "Please input confirm new pass", Toast.LENGTH_SHORT).show()
            edit_cfnewpassfield.requestFocus()
        } else if (!newPass.equals(cfNewPass)) {
            Toast.makeText(this, "Confirmation of the new password mismatch!", Toast.LENGTH_SHORT).show()
            edit_cfnewpassfield.requestFocus()
        }
        else {
            changePassword(email, oldPass, newPass)
        }
    }

    fun changePassword(email: String, oldPass: String, newPass: String) {
        user = FirebaseAuth.getInstance().currentUser!!;

        var credential: AuthCredential = EmailAuthProvider.getCredential(email, oldPass)

        user.reauthenticate(credential)
            .addOnCompleteListener(object: OnCompleteListener<Void> {
                override fun onComplete(@NonNull task: Task<Void>) {
                    if (task.isSuccessful())
                    {
                        user.updatePassword(newPass).addOnCompleteListener(object:OnCompleteListener<Void> {
                            override fun onComplete(@NonNull task:Task<Void>) {
                                if (task.isSuccessful())
                                {
                                    val builder = AlertDialog.Builder(this@ManageAccount)
                                    builder.setTitle("Change Password")
                                    builder.setMessage("Password Updated!")
                                    builder.setPositiveButton("OK"){dialogInterface, which ->
                                        finish();
                                        startActivity(getIntent());
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.show()
                                }
                                else
                                {
                                    val builder = AlertDialog.Builder(this@ManageAccount)
                                    builder.setTitle("Change Password")
                                    builder.setMessage("Password Update Failed!")
                                    builder.setPositiveButton("OK"){dialogInterface, which ->
                                        Toast.makeText(applicationContext,"Please try again",Toast.LENGTH_LONG).show()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.show()
                                }
                            }
                        })
                    }
                    else
                    {
                        val builder = AlertDialog.Builder(this@ManageAccount)
                        builder.setTitle("Change Password")
                        builder.setMessage("Authentication failed!")
                        builder.setPositiveButton("OK"){dialogInterface, which ->
                            Toast.makeText(applicationContext,"Please try again",Toast.LENGTH_LONG).show()
                        }
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    }
                }
            })
    }
}
