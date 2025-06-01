package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignupRedirect: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignupRedirect = findViewById(R.id.tvSignupRedirect)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Handle Login Button
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, UserHomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Redirect to Signup Page
        tvSignupRedirect.setOnClickListener {
            val intent = Intent(this, UserSignupActivity::class.java)
            startActivity(intent)
        }
    }
}
