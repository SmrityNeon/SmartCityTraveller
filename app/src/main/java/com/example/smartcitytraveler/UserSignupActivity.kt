package com.example.smartcitytraveler


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserSignupActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var imgLogo: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)

        // Initialize views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.btnSignup)
        imgLogo = findViewById(R.id.imgLogo) // optional - in case you want to add click or animation

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        // Sign Up button click listener
        btnSignup.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val userMap = mapOf("name" to name, "email" to email)
                        dbRef.reference.child("users").child(uid).setValue(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, UserHomeActivity::class.java))
                                finish()
                            }
                    } else {
                        Toast.makeText(this, "Signup failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
