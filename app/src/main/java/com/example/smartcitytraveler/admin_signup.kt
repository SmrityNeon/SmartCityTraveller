package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class admin_signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase

    private lateinit var etAdminName: EditText
    private lateinit var etAdminEmail: EditText
    private lateinit var etAdminPassword: EditText
    private lateinit var btnAdminSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_signup)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        etAdminName = findViewById(R.id.etAdminName)
        etAdminEmail = findViewById(R.id.etAdminEmail)
        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnAdminSignup = findViewById(R.id.btnAdminSignup)

        btnAdminSignup.setOnClickListener {
            val name = etAdminName.text.toString().trim()
            val email = etAdminEmail.text.toString().trim()
            val password = etAdminPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            val admin = mapOf(
                                "name" to name,
                                "email" to email
                            )
                            if (uid != null) {
                                dbRef.reference.child("admins").child(uid).setValue(admin)
                                    .addOnCompleteListener {
                                        Toast.makeText(this, "Admin Registered", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, AdminDashboardActivity::class.java))
                                        finish()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}
