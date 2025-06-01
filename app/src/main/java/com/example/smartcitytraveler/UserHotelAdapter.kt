package com.example.smartcitytraveler
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcitytraveler.HotelModel

class UserHotelAdapter(private val hotels: List<HotelModel>) :
    RecyclerView.Adapter<UserHotelAdapter.HotelViewHolder>() {

    inner class HotelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvHotelName)
        val tvLocation: TextView = view.findViewById(R.id.tvHotelLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvHotelPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item_user, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotels[position]
        holder.tvName.text = hotel.name
        holder.tvLocation.text = hotel.location
        holder.tvPrice.text = "৳${hotel.price}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UserBookingActivity::class.java)
            intent.putExtra("hotelId", hotel.hotelId)
            intent.putExtra("name", hotel.name)
            intent.putExtra("location", hotel.location)
            intent.putExtra("price", hotel.price)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = hotels.size
}
