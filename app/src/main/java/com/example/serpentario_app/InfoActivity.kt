package com.example.serpentario_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        TitleCard.setOnClickListener {
            goEntiRespctivity()
        }

        DescriptionCard.setOnClickListener {
            goEntiRespctivity()
        }


    }

    private fun goEntiRespctivity() {
        val intent = Intent(this, EntiRespActivity::class.java)
        startActivity(intent)
    }
}