package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class select_activity : AppCompatActivity() {

    private lateinit var btnUserLogin: Button
    private lateinit var btnAdminLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        btnUserLogin = findViewById(R.id.btnUserLogin)
        btnAdminLogin = findViewById(R.id.btnAdminLogin)

        btnUserLogin.setOnClickListener {
            // Corrected: use 'this' instead of 'Activity'
            startActivity(Intent(this, UserLoginActivity::class.java))
        }

        btnAdminLogin.setOnClickListener {
            startActivity(Intent(this, admin_login::class.java))
        }
    }
}
