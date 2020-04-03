package com.example.wirelessmatchinggame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "CreateAccount"

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
            val builder = AlertDialog.Builder(this@CreateAccount)
            builder.setTitle("Register")
            builder.setMessage("Please input email!")
            builder.setPositiveButton("OK"){dialogInterface, which ->
                regis_emailField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        } else if (password.length <= 0) {
            val builder = AlertDialog.Builder(this@CreateAccount)
            builder.setTitle("Register")
            builder.setMessage("Please input password!")
            builder.setPositiveButton("OK"){dialogInterface, which ->
                regis_passwordField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        } else if (confirmPass.length <= 0) {
            val builder = AlertDialog.Builder(this@CreateAccount)
            builder.setTitle("Register")
            builder.setMessage("Please input confirm password!")
            builder.setPositiveButton("OK"){dialogInterface, which ->
                regis_confirmPassField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        } else if (!password.equals(confirmPass)) {
            val builder = AlertDialog.Builder(this@CreateAccount)
            builder.setTitle("Register")
            builder.setMessage("Password mismatch!")
            builder.setPositiveButton("OK"){dialogInterface, which ->
                regis_confirmPassField.requestFocus()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        else {
            createAccount(email, password)
        }


    }

    fun createAccount(email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val builder = AlertDialog.Builder(this@CreateAccount)
                    builder.setTitle("Register")
                    builder.setMessage("Registration successful!")
                    builder.setPositiveButton("OK"){dialogInterface, which ->
                        //TODO redirect to login page
                        val loginIntent = Intent(this, MainActivity::class.java)
                        startActivity(loginIntent)
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                } else {
                    Log.w(TAG, "createAccountWithEmail:failure", task.exception)
                    if(task.exception is FirebaseAuthWeakPasswordException) {
                        val builder = AlertDialog.Builder(this@CreateAccount)
                        builder.setTitle("Register")
                        builder.setMessage("Registration failed : Weak password used! Please input more than 6 characters!")
                        builder.setPositiveButton("OK"){dialogInterface, which ->
                            regis_passwordField.requestFocus()
                        }
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    } else {
                        val builder = AlertDialog.Builder(this@CreateAccount)
                        builder.setTitle("Register")
                        builder.setMessage("Registration failed!")
                        builder.setPositiveButton("OK"){dialogInterface, which ->
                            regis_emailField.requestFocus()
                        }
                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    }
                }

                // EXCLUDE
                if (!task.isSuccessful) {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
