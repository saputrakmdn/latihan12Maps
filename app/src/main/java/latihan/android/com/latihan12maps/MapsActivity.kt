package latihan.android.com.latihan12maps

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?)= false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var isFABOpen = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val normal = findViewById<FloatingActionButton>(R.id.fab1)
        val satelit = findViewById<FloatingActionButton>(R.id.fab2)
        val terain = findViewById<FloatingActionButton>(R.id.fab3)
        normal.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        satelit.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        terain.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }

    private fun showUp(){
        isFABOpen=true
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55))
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105))
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155))
    }
    private fun showDown(){
        isFABOpen=false
        fab1.animate().translationY(0f)
        fab2.animate().translationY(0f)
        fab3.animate().translationY(0f)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-6.1648944,106.7629343)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Jakbar"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        setUpMap()
        fab.setOnClickListener {
            if(!isFABOpen){
                showUp()
            }else{
                showDown()
            }
        }
    }
    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE )
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this){
            location ->
            if(location != null){
                lastLocation = location
                val currentPos = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentPos)
                mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(currentPos, 18.0f)))
            }
        }

        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID


    } private fun placeMarkerOnMap(loc: LatLng){
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_narto)))
        mMap.addMarker(markerOptions)}
}
