package com.example.smartcitytraveler


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(private val bookings: List<BookingModel>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHotelName: TextView = view.findViewById(R.id.tvHotelName)
        val tvBookingDetails: TextView = view.findViewById(R.id.tvBookingDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_item, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.tvHotelName.text = booking.hotelName
        holder.tvBookingDetails.text =
            "Location: ${booking.location}\nRoom: ${booking.roomType}\nDate: ${booking.date}"
    }

    override fun getItemCount(): Int = bookings.size
}
