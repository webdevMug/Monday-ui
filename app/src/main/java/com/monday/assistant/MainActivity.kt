package com.monday.assistant

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var active = false
    private lateinit var wave: WaveView
    private lateinit var status: TextView

    private val fakeVoice = object : Runnable {
        override fun run() {
            if (active) {
                wave.setLevel(Random.nextFloat())
                handler.postDelayed(this, 120)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wave = findViewById(R.id.waveView)
        status = findViewById(R.id.status)

        wave.setOnClickListener {
            active = !active
            if (active) {
                status.text = "Listening..."
                handler.post(fakeVoice)
            } else {
                status.text = "Tap to wake"
                wave.setLevel(0.15f)
            }
        }
    }
}
