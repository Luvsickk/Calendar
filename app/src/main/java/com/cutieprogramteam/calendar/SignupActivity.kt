package com.cutieprogramteam.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cutieprogramteam.calendar.databinding.SignupBinding
class SignupActivity : AppCompatActivity(){
    private lateinit var binding: SignupBinding
    private lateinit var db: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            signupDatabase(signupUsername, signupPassword)
        }
        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

        private fun signupDatabase(username: String, password: String) {
            val insertedRowId = db.insertUser(username, password)
            if (insertedRowId != -1L) {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
            }

        }
    }