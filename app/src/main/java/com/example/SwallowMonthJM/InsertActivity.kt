package com.example.SwallowMonthJM

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.SwallowMonthJM.databinding.ActivityInsertBinding

class InsertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}