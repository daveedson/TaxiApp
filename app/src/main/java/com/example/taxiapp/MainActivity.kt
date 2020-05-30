package com.example.taxiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    private lateinit var   handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        handler = Handler()

        handler.postDelayed({


            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        },4000// delaying 4secs to open mainActivity


        )

    }
}
