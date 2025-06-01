package com.example.smartcitytraveler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminViewBookingsActivity : AppCompatActivity() {

    private lateinit var recyclerBookings: RecyclerView
    private lateinit var bookingList: ArrayList<BookingModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_bookings)

        recyclerBookings = findViewById(R.id.recyclerBookings)
        recyclerBookings.layoutManager = LinearLayoutManager(this)

        bookingList = arrayListOf()
        adapter = BookingAdapter(bookingList)
        recyclerBookings.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("bookings")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (userSnap in snapshot.children) {
                    for (bookingSnap in userSnap.children) {
                        val booking = bookingSnap.getValue(BookingModel::class.java)
                        if (booking != null) {
                            bookingList.add(booking)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminViewBookingsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
