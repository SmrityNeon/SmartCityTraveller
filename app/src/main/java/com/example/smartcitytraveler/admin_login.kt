package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class admin_login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etAdminEmail: EditText
    private lateinit var etAdminPassword: EditText
    private lateinit var btnAdminLogin: Button
    private lateinit var tvSignupRedirect: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        auth = FirebaseAuth.getInstance()

        // Initialize views
        etAdminEmail = findViewById(R.id.etAdminEmail)
        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnAdminLogin = findViewById(R.id.btnAdminLogin)
        tvSignupRedirect = findViewById(R.id.tvSignupRedirect)

        // Login button click
        btnAdminLogin.setOnClickListener {
            val email = etAdminEmail.text.toString().trim()
            val password = etAdminPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Admin Logged In", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        // Signup redirect
        tvSignupRedirect.setOnClickListener {
            val intent = Intent(this, admin_signup::class.java)
            startActivity(intent)
        }
    }
}
