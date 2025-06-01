package com.example.smartcitytraveler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminManageHotelsActivity : AppCompatActivity() {

    private lateinit var recyclerHotels: RecyclerView
    private lateinit var hotelList: ArrayList<HotelModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: HotelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_hotels)

        recyclerHotels = findViewById(R.id.recyclerHotels)
        recyclerHotels.layoutManager = LinearLayoutManager(this)

        hotelList = arrayListOf()
        adapter = HotelAdapter(this, hotelList)
        recyclerHotels.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("hotels")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hotelList.clear()
                for (hotelSnap in snapshot.children) {
                    val hotel = hotelSnap.getValue(HotelModel::class.java)
                    if (hotel != null) {
                        hotelList.add(hotel)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminManageHotelsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
