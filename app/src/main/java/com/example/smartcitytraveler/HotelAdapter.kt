package com.example.smartcitytraveler

import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class HotelAdapter(
    private val context: Context,
    private val hotelList: List<HotelModel>
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("hotels")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotelList[position]
        holder.tvName.text = hotel.name
        holder.tvLocation.text = hotel.location
        holder.tvPrice.text = "৳${hotel.price}"

        holder.btnEdit.setOnClickListener {
            showUpdateDialog(hotel)
        }

        holder.btnDelete.setOnClickListener {
            dbRef.child(hotel.hotelId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Hotel deleted", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int = hotelList.size

    inner class HotelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvHotelName)
        val tvLocation: TextView = view.findViewById(R.id.tvHotelLocation)
        val tvPrice: TextView = view.findViewById(R.id.tvHotelPrice)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    private fun showUpdateDialog(hotel: HotelModel) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_admin_add_hotel, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).setTitle("Update Hotel").create()

        val etName = dialogView.findViewById<EditText>(R.id.etHotelName)
        val etLocation = dialogView.findViewById<EditText>(R.id.etHotelLocation)
        val etPrice = dialogView.findViewById<EditText>(R.id.etHotelPrice)
        val etImageUrl = dialogView.findViewById<EditText>(R.id.etHotelImageUrl)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveHotel)

        etName.setText(hotel.name)
        etLocation.setText(hotel.location)
        etPrice.setText(hotel.price)
        etImageUrl.setText(hotel.imageUrl)

        btnSave.setOnClickListener {
            val updatedHotel = mapOf(
                "hotelId" to hotel.hotelId,
                "name" to etName.text.toString(),
                "location" to etLocation.text.toString(),
                "price" to etPrice.text.toString(),
                "imageUrl" to etImageUrl.text.toString()
            )
            dbRef.child(hotel.hotelId).updateChildren(updatedHotel).addOnSuccessListener {
                Toast.makeText(context, "Hotel updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
