package com.cutieprogramteam.calendar

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import com.cutieprogramteam.calendar.databinding.CalendarlayoutBinding
import com.cutieprogramteam.calendar.databinding.PlaceholdermenuBinding
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import androidx.core.content.ContextCompat
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    private lateinit var binding: PlaceholdermenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = PlaceholdermenuBinding.inflate(layoutInflater)


        binding.btn1.setOnClickListener {
            navigateToScreenOne()
        }
        setContentView(binding.root)

        binding.btn2.setOnClickListener {
            navigateToScreenTwo()
        }
        setContentView(binding.root)

        binding.btn3.setOnClickListener {
            navigateToScreenThree()
        }
        setContentView(binding.root)

        binding.btn4.setOnClickListener {
            navigateToScreenFour()
        }
        setContentView(binding.root)


    }
    private fun navigateToScreenOne() {
        val intent = Intent(this,CalendarActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToScreenTwo() {
        val intent = Intent(this, PomodoroActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToScreenThree() {
        val intent = Intent(this, Jude::class.java)
        startActivity(intent)
    }

    private fun navigateToScreenFour() {
        val intent = Intent(this, TodolistActivity::class.java)
        startActivity(intent)
    }

}

