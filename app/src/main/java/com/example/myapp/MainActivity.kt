package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var mMap:GoogleMap
    var UserLocation = LatLng(53.919297334356116, 27.592509945391342)

    object DataHolder{
        var data: Car = Car("0","0",0,0.0,0.0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
    }

    private fun checkPermission() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешения предоставлены, можно продолжать работу с местоположением
            } else {
                // Разрешения не предоставлены, показываем сообщение пользователю
                Toast.makeText(this, "Необходимо разрешение на доступ к местоположению", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isBuildingsEnabled = true
        mMap.isIndoorEnabled = true

        var Car1Location = LatLng(53.92063044329027, 27.596453684334257)
        var Car2Location = LatLng(53.9211354378163, 27.590907178440855)

        val UserMarkerOptions = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker))
        val CarMarkerOptions = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker))

        val markers = mutableListOf<Marker>()

        val marker1 = mMap.addMarker(UserMarkerOptions.position(UserLocation))
        val marker2 = mMap.addMarker(CarMarkerOptions.position(Car1Location))
        val marker3 = mMap.addMarker(CarMarkerOptions.position(Car2Location))

        markers.add(marker1!!)
        markers.add(marker2!!)
        markers.add(marker3!!)

        setCameraLocation()

        //Обработчик нажатия на маркер
        mMap.setOnMarkerClickListener { marker ->
            val index = markers.indexOf(marker)
            if(index == 0) Toast.makeText(this, "Вы находитесь здесь", Toast.LENGTH_LONG).show()
            else findCar(index)
            true
        }
    }

    //Переход на страницу профиля
    fun onClickToProfile(view : View){
        val intent = Intent(this, ActivitySecond::class.java)
        startActivity(intent)
    }

    //Сохранение параметров камеры при переходе на новую страницу
    override fun onPause() {
        super.onPause()
        val cameraPosition = mMap.cameraPosition
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putFloat("LATITUDE", cameraPosition.target.latitude.toFloat())
            putFloat("LONGITUDE", cameraPosition.target.longitude.toFloat())
            putFloat("ZOOM", cameraPosition.zoom)
            apply()
        }
    }

    //Возвращение параметров камеры после возвражения
    private fun setCameraLocation(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val latitude = sharedPref.getFloat("LATITUDE", 0.0f).toDouble()
        val longitude = sharedPref.getFloat("LONGITUDE", 0.0f).toDouble()
        val zoom = sharedPref.getFloat("ZOOM", 0.0f)
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(latitude, longitude))
            .zoom(zoom)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    //Приближение камеры
    fun cameraZoomIn(view: View){
        val currentZoomLevel = mMap.cameraPosition.zoom
        val newZoomLevel = currentZoomLevel + 1
        mMap.animateCamera(CameraUpdateFactory.zoomTo(newZoomLevel))
    }

    //Отдаление камеры
    fun cameraZoomOut(view: View){
        val currentZoomLevel = mMap.cameraPosition.zoom
        val newZoomLevel = currentZoomLevel - 1
        mMap.animateCamera(CameraUpdateFactory.zoomTo(newZoomLevel))
    }

    //Поиск местоположения пользователя
    fun findUserLocation(view: View){
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        mMap.animateCamera(CameraUpdateFactory.newLatLng(UserLocation))
    }

    //получение информации о машине
    private fun findCar(index: Int) {
        //Создание БД
        val dbCar = DbCar(this, null)

        //Добавление машин в БД
        dbCar.addCar(Car("1","Lada",7734,0.55,0.20,75))
        dbCar.addCar(Car("2","Skoda",3241,0.6,0.25,12))

        val car = dbCar.getCar(index.toString())

        DataHolder.data = car

        //Инициализация bottomsheet
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}