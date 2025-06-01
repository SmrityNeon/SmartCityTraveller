package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnAddHotel: Button
    private lateinit var btnManageHotels: Button
    private lateinit var btnViewBookings: Button
    private lateinit var btnLogout: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("admins")

        tvWelcome = findViewById(R.id.tvWelcome)
        btnAddHotel = findViewById(R.id.btnAddHotel)
        btnManageHotels = findViewById(R.id.btnManageHotels)
        btnViewBookings = findViewById(R.id.btnViewBookings)
        btnLogout = findViewById(R.id.btnLogout)

        val uid = auth.currentUser?.uid
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener {
                val name = it.child("name").value.toString()
                tvWelcome.text = "Welcome, $name"
            }
        }

        btnAddHotel.setOnClickListener {
            startActivity(Intent(this, AdminAddHotelActivity::class.java))
        }

        btnManageHotels.setOnClickListener {
            startActivity(Intent(this, AdminManageHotelsActivity::class.java))
        }

        btnViewBookings.setOnClickListener {
            startActivity(Intent(this, AdminViewBookingsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, select_activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
