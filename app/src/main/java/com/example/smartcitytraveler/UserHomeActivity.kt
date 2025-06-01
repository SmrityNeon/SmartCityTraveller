package com.example.smartcitytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.widget.SearchView

class UserHomeActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var searchView: SearchView
    private lateinit var recyclerHotels: RecyclerView
    private lateinit var btnViewBookings: Button
    private lateinit var btnLogout: Button

    private lateinit var hotelList: ArrayList<HotelModel>
    private lateinit var fullHotelList: ArrayList<HotelModel>
    private lateinit var adapter: UserHotelAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        tvWelcome = findViewById(R.id.tvWelcome)
        searchView = findViewById(R.id.searchView)
        recyclerHotels = findViewById(R.id.recyclerHotels)
        btnViewBookings = findViewById(R.id.btnViewBookings)
        btnLogout = findViewById(R.id.btnLogout)

        recyclerHotels.layoutManager = LinearLayoutManager(this)
        hotelList = arrayListOf()
        fullHotelList = arrayListOf()
        adapter = UserHotelAdapter(hotelList)
        recyclerHotels.adapter = adapter

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("hotels")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hotelList.clear()
                fullHotelList.clear()
                for (hotelSnap in snapshot.children) {
                    val hotel = hotelSnap.getValue(HotelModel::class.java)
                    if (hotel != null) {
                        hotelList.add(hotel)
                        fullHotelList.add(hotel)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserHomeActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = fullHotelList.filter {
                    it.name?.contains(newText ?: "", ignoreCase = true) == true
                }
                hotelList.clear()
                hotelList.addAll(filteredList)
                adapter.notifyDataSetChanged()
                return true
            }
        })

        btnViewBookings.setOnClickListener {
            startActivity(Intent(this, UserBookingHistoryActivity::class.java))
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
