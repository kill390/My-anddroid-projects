package com.example.android.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var number = 0
    fun plus(view: View) {
        number ++
        var text = findViewById<TextView>(R.id.textView)
        text.setText(number.toString())
    }

    fun reset(view: View) {
        number =0
        var text = findViewById<TextView>(R.id.textView)
        text.setText(number.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}


