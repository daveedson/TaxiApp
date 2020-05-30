package com.example.taxiapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.*
import java.security.Permission
import java.util.jar.Manifest

class PassengerActivity : AppCompatActivity(), OnMapReadyCallback,View.OnClickListener {

    private lateinit var mMap: GoogleMap    //CREATION of instance variable for googleMaps...
    private lateinit var locationManager: LocationManager     //Instance variable creation for locationManager...
    private lateinit var locationListener: LocationListener //Instance variable creation for locationListener, is used to access the location of the passenger....
    private  lateinit var btnRequestRide:Button
    private   var rideCanceled :Boolean = true // instance variable to set the cancel ride functionality




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_activity)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnRequestRide = findViewById(R.id.btnRequestRide)

        btnRequestRide.setOnClickListener(this)

        var carRequestQuery = ParseQuery.getQuery<ParseObject>("RequestRide") // creating a query from the RequestRide class

        carRequestQuery.whereEqualTo("username",ParseUser.getCurrentUser().username) // the car request query should give us the value of the username

        //finds the user in the background
        carRequestQuery.findInBackground(FindCallback { objects, e ->

            /*condition to check if the objects in our db is greater than zero and if there ,
            isn't any error /exception and if we get a valid objects in our db we set the rideCanceled variable to false.
            that is the ride should not be canceled.
             */
            if (objects.size>0 && e == null){
                    rideCanceled = false
                btnRequestRide.text = "Cancel your Ride"
            }

        })

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //this function initialize our Location manger and location listener
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        locationManager  = getSystemService(Context.LOCATION_SERVICE) as LocationManager // setting the context of location manager


        var locationListener  = object : LocationListener {
            //this method would be called every time the user changes his location
            override fun onLocationChanged(location: Location?) {

                updateCameraPassengerLocation(location!!)

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

       //  This block asks for Location permissions
      if (Build.VERSION.SDK_INT<23){
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
      }else if (Build.VERSION.SDK_INT >= 23){
          if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
              ActivityCompat.requestPermissions(this, arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
          }else{
              locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

              var currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
              updateCameraPassengerLocation(currentPassengerLocation)
          }
      }



//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //this method handles the result gotten back from the user either he gave us the permission or denied us the permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1000 && grantResults.size > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            var locationListener  = object : LocationListener{
                override fun onLocationChanged(location: Location?) {
                    updateCameraPassengerLocation(location!!)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }
            }


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
                var currentPassengerLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                updateCameraPassengerLocation(currentPassengerLocation)
            }

        }
    }

    //this function uses the users camera to fetch the current location
    private fun updateCameraPassengerLocation(plocation:Location){

        var passengerLocation = LatLng(plocation.latitude,plocation.longitude)
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(passengerLocation,15f))
        mMap.addMarker(MarkerOptions().position(passengerLocation).title("You are here"))
    }

    //The onclick method of the button
    override fun onClick(v: View?) {

        if (rideCanceled){




        /*

        //this is the initializing of the location listener so it could be used in side the onclick method of the button
         var locationListener  = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                    updateCameraPassengerLocation(location!!)}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) { }
        }

         */
        var locationListener  = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                    updateCameraPassengerLocation(location!!)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }

        //access permission of users
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { //first check for access of device location
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            ) //ask for the location update
            var passengerCurrentLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (passengerCurrentLocation != null) {

                var requestRide =
                    ParseObject("RequestRide") //instance creation of parse object/ class name
                requestRide.put("username", ParseUser.getCurrentUser().username)

                var userLocation = ParseGeoPoint(
                    passengerCurrentLocation.latitude,
                    passengerCurrentLocation.longitude
                )// getting the location of the of the user using ParseGeo Point which takes an input of longitude and latitude

                requestRide.put("passengerLocation", userLocation)

                //saves the the object in the background inside the Db
                requestRide.saveInBackground { e: ParseException? ->
                    if (e == null) {
                        Toast.makeText(this, "Your Ride Request has been Sent", Toast.LENGTH_LONG)
                            .show()

                        btnRequestRide.text =
                            "Cancel your ride" //changes the text of the button after request has been sent..
                        rideCanceled = false

                    } else {
                        Toast.makeText(this, " $e", Toast.LENGTH_LONG).show()

                    }
                }

            } else {
                Toast.makeText(this, "Unknown error something went wrong !", Toast.LENGTH_LONG)
                    .show()
            }
        }
        }else{

             var carRequestQuery = ParseQuery.getQuery<ParseObject>("RequestRide")
                carRequestQuery.whereEqualTo("username",ParseUser.getCurrentUser().username)
            carRequestQuery.findInBackground(FindCallback { rideRequests, e ->

                if (rideRequests.size> 0 && e == null){

                    rideCanceled = true
                    btnRequestRide.setText("Request a new Ride")

                    for (rideRequest in rideRequests){

                        rideRequest.deleteInBackground(DeleteCallback {
                            if (it == null){
                                Toast.makeText(this,"Ride has been Canceled",Toast.LENGTH_LONG).show()
                            }
                        })

                    }
                }
            })
        }
    }
}
