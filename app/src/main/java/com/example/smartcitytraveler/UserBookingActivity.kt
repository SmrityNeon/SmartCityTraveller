package com.example.smartcitytraveler


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class UserBookingActivity : AppCompatActivity() {

    private lateinit var tvHotelName: TextView
    private lateinit var tvHotelLocation: TextView
    private lateinit var tvHotelPrice: TextView
    private lateinit var etBookingDate: EditText
    private lateinit var spinnerRoomType: Spinner
    private lateinit var btnBookNow: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase

    private lateinit var hotelId: String
    private lateinit var hotelName: String
    private lateinit var location: String
    private lateinit var price: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_booking)

        // Firebase
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        // Get views
        tvHotelName = findViewById(R.id.tvHotelName)
        tvHotelLocation = findViewById(R.id.tvHotelLocation)
        tvHotelPrice = findViewById(R.id.tvHotelPrice)
        etBookingDate = findViewById(R.id.etBookingDate)
        spinnerRoomType = findViewById(R.id.spinnerRoomType)
        btnBookNow = findViewById(R.id.btnBookNow)

        // Get data from intent
        hotelId = intent.getStringExtra("hotelId") ?: ""
        hotelName = intent.getStringExtra("name") ?: ""
        location = intent.getStringExtra("location") ?: ""
        price = intent.getStringExtra("price") ?: ""

        tvHotelName.text = hotelName
        tvHotelLocation.text = location
        tvHotelPrice.text = "৳$price"

        // Spinner options
        val roomTypes = arrayOf("Single", "Double", "Deluxe", "Suite")
        spinnerRoomType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roomTypes)

        // Date Picker
        etBookingDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, day ->
                etBookingDate.setText("$year-${month + 1}-$day")
            }, y, m, d)

            datePicker.show()
        }

        // Book Now
        btnBookNow.setOnClickListener {
            val date = etBookingDate.text.toString().trim()
            val roomType = spinnerRoomType.selectedItem.toString()

            if (date.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val bookingId = dbRef.reference.child("bookings").child(userId).push().key!!

            val bookingData = mapOf(
                "bookingId" to bookingId,
                "hotelId" to hotelId,
                "hotelName" to hotelName,
                "location" to location,
                "date" to date,
                "roomType" to roomType
            )

            dbRef.reference.child("bookings").child(userId).child(bookingId)
                .setValue(bookingData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hotel booked successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}
