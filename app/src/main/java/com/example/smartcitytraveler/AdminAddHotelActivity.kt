package com.example.smartcitytraveler

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AdminAddHotelActivity : AppCompatActivity() {

    private lateinit var etHotelName: EditText
    private lateinit var etHotelLocation: EditText
    private lateinit var etHotelPrice: EditText
    private lateinit var etHotelImageUrl: EditText
    private lateinit var btnSaveHotel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_hotel)

        etHotelName = findViewById(R.id.etHotelName)
        etHotelLocation = findViewById(R.id.etHotelLocation)
        etHotelPrice = findViewById(R.id.etHotelPrice)
        etHotelImageUrl = findViewById(R.id.etHotelImageUrl)
        btnSaveHotel = findViewById(R.id.btnSaveHotel)

        val dbRef = FirebaseDatabase.getInstance().getReference("hotels")

        btnSaveHotel.setOnClickListener {
            val name = etHotelName.text.toString().trim()
            val location = etHotelLocation.text.toString().trim()
            val price = etHotelPrice.text.toString().trim()
            val imageUrl = etHotelImageUrl.text.toString().trim()

            if (name.isEmpty() || location.isEmpty() || price.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hotelId = dbRef.push().key!!
            val hotel = mapOf(
                "hotelId" to hotelId,
                "name" to name,
                "location" to location,
                "price" to price,
                "imageUrl" to imageUrl
            )

            dbRef.child(hotelId).setValue(hotel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Hotel saved successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearFields() {
        etHotelName.text.clear()
        etHotelLocation.text.clear()
        etHotelPrice.text.clear()
        etHotelImageUrl.text.clear()
    }
}
