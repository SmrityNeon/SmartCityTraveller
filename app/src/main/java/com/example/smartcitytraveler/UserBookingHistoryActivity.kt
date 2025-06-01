package com.example.smartcitytraveler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserBookingHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerMyBookings: RecyclerView
    private lateinit var bookingList: ArrayList<BookingModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_booking_history)

        recyclerMyBookings = findViewById(R.id.recyclerMyBookings)
        recyclerMyBookings.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("bookings")

        val userId = auth.currentUser?.uid ?: return
        bookingList = arrayListOf()
        adapter = BookingAdapter(bookingList)
        recyclerMyBookings.adapter = adapter

        dbRef.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (bookingSnap in snapshot.children) {
                    val booking = bookingSnap.getValue(BookingModel::class.java)
                    if (booking != null) {
                        bookingList.add(booking)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserBookingHistoryActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class BookingAdapter(private val bookings: List<BookingModel>) :
        RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvHotelName: TextView = view.findViewById(R.id.tvHotelName)
            val tvBookingDetails: TextView = view.findViewById(R.id.tvBookingDetails)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.booking_item_user, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val booking = bookings[position]
            holder.tvHotelName.text = booking.hotelName
            holder.tvBookingDetails.text =
                "Location: ${booking.location}\nRoom: ${booking.roomType}\nDate: ${booking.date}"
        }

        override fun getItemCount(): Int = bookings.size
    }
}
