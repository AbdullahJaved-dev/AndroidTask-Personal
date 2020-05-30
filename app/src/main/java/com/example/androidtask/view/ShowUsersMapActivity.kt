package com.example.androidtask.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.emedinaa.kotlinmvvm.di.Injection
import com.example.androidtask.R
import com.example.androidtask.model.User
import com.example.androidtask.viewmodel.UserViewModel
import com.example.androidtask.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class ShowUsersMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewModel: UserViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    var usersList: ArrayList<User> = ArrayList()

    companion object {
        const val TAG = "CONSOLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.view!!
        setupViewModel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!
        mMap.uiSettings.isCompassEnabled = false
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(Injection.providerRepository()))
            .get(UserViewModel::class.java)
        viewModel.users.observe(this, renderUsers)

        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.isEmptyList.observe(this, emptyListObserver)
    }

    //observers
    private val renderUsers = Observer<List<User>> {
        Log.v(ShowUsersListActivity.TAG, "data updated $it")
        var builder = LatLngBounds.Builder();

        for (entry in it) {
            usersList.add(entry)
            val lartLng = LatLng(
                entry.address.geo.lat.toDouble(),
                entry.address.geo.lng.toDouble()
            )
            mMap.addMarker(
                MarkerOptions().position(lartLng).title(
                    entry.name
                ).snippet(entry.name + "*-*" + entry.email + "*-*" + entry.address.city).icon(
                    bitmapDescriptorFromVector(
                        applicationContext,
                        R.drawable.social
                    )
                )
            )
            builder.include(lartLng)
        }
    }
    private val isViewLoadingObserver = Observer<Boolean> {
        Log.v(ShowUsersListActivity.TAG, "isViewLoading $it")
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(ShowUsersListActivity.TAG, "onMessageError $it")
        //  textViewError.text= "Error $it"
    }

    private val emptyListObserver = Observer<Boolean> {
        Log.v(ShowUsersListActivity.TAG, "emptyListObserver $it")
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}
