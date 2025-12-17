package com.example.ca1giftcardwr.views.editlocation

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.models.Location
import java.util.Locale

class EditLocationView : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter
    var location = Location()
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        presenter = EditLocationPresenter(this)

        location = intent.extras?.getParcelable<Location>("location")
            ?: Location()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //setup search
        val searchInput = findViewById<EditText>(R.id.searchLocation)
        val searchButton = findViewById<Button>(R.id.btnSearch)

        searchButton.setOnClickListener {
            val searchText = searchInput.text.toString().trim()
            if (searchText.isNotEmpty()) {
                searchLocation(searchText)
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }

        val setLocationButton = findViewById<Button>(R.id.btnSetLocation)
        setLocationButton.setOnClickListener {
            presenter.doOnBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            presenter.doOnBackPressed()
        }
    }

    private fun searchLocation(address: String) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())

            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(address, 5)

            if (!addresses.isNullOrEmpty()) {
                val result = addresses[0]
                val latLng = LatLng(result.latitude, result.longitude)

                location.lat = result.latitude
                location.lng = result.longitude
                location.zoom = 17f

                val addressText = result.getAddressLine(0) ?: address
                findViewById<EditText>(R.id.searchLocation).setText(addressText)
                currentMarker?.remove()
                currentMarker = map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Store Location")
                        .snippet(addressText)
                        .draggable(true)
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Toast.makeText(this, "Location found!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location not found. Try different keywords.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error searching: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val loc = LatLng(location.lat, location.lng)
        currentMarker = map.addMarker(
            MarkerOptions()
                .position(loc)
                .title("Store Location")
                .snippet("GPS: ${location.lat}, ${location.lng}")
                .draggable(true)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)

        map.uiSettings.isZoomControlsEnabled = true
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        marker.snippet = "GPS: ${location.lat}, ${location.lng}"
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.snippet = "GPS: ${location.lat}, ${location.lng}"
        return false
    }
}