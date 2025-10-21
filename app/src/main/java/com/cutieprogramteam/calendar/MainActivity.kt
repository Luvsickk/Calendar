package com.cutieprogramteam.calendar

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.compose.material3.Button
import androidx.compose.ui.platform.ComposeView
import com.cutieprogramteam.calendar.databinding.CalendarlayoutBinding
import com.cutieprogramteam.calendar.databinding.PlaceholdermenuBinding
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
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
    }

}

private fun navigateToScreenOne() {
    val intent = Intent(this, ScreenOneActivity::class.java)

    startActivity(intent)
}
