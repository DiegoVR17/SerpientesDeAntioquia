package com.example.serpentario_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import com.example.serpentario_app.Model.Serpiente
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_avistamiento.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.jar.Manifest

private const val REQUEST_CODE = 3
private lateinit var photoFile: File
@Suppress("DEPRECATION")
class AvistamientoActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    private var PERMISSION_ID = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avistamiento)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()


        val date = Date()

        var Nombre = NombreSerp.text.toString()



        BtnTomarFoto.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(takePictureIntent.resolveActivity(this.packageManager) != null){
                startActivityForResult(takePictureIntent,REQUEST_CODE)
            }else{
                Toast.makeText(this,"Error acceso a cámara",Toast.LENGTH_SHORT).show()
            }

        }

        BtnGuardar.setOnClickListener {
            var storage = FirebaseStorage.getInstance()
            var tipo = ""
            var estado = ""
            var ambiente = ""
            var lugar = ""

//
//            if (switch_viva.isChecked) {
//                tipo += "Venenosa"
//            } else {
//                tipo += "No venenosa"
//            }

            if (switch_tipo.isChecked) {
                tipo += "Venenosa"
            } else {
                tipo += "No venenosa"
            }

            if(Viva.isChecked){
                estado += "Viva"
            }
            if(Muerta.isChecked){
                estado += "Muerta"
            }
            if(Indeterminado.isChecked){
                estado += "Inderminado"
            }


                if(Soleado.isChecked) {
                    ambiente += "Soleado"
                    Nublado.isEnabled = false
                    Lluvioso.isEnabled = false
                }
                if (Nublado.isChecked) {
                    ambiente += "Nublado"
                    Soleado.isEnabled = false
                    Lluvioso.isEnabled = false
                }
                if(Lluvioso.isChecked) {
                    ambiente += "Lluvioso"
                    Nublado.isEnabled = false
                    Soleado.isEnabled = false
                }

                if(Vivienda.isChecked){
                    lugar +="Vivienda"
                }
                if (Carretera.isChecked){
                    lugar +="Carretera"
                }
                if(Campo.isChecked){
                    lugar +="Campo"
                }
                if(Cultivo.isChecked){
                    lugar +="Cultivo"
                }
                if(Potrero.isChecked){
                    lugar +="Potrero"
                }
                if(Bosque.isChecked){
                    lugar +="Bosque"
                }
                if(Quebrada.isChecked){
                    lugar +="Quebrada"
                }

            if(Nombre == ""){
                Nombre +="Por defecto"
            }

            val photoRef = storage.reference.child("serpientes").child(Nombre)
                val bitmap = (imageViewFoto.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
                val data = baos.toByteArray()

                var uploadTask = photoRef.putBytes(data)

                val urlTask =
                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {task ->
                        if(!task.isSuccessful){
                            task.exception.let {
                                throw it!!
                            }
                        }
                        return@Continuation photoRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            saveUser(tipo, estado, ambiente, lugar, downloadUri.toString(),date.toString())
                        }
                    }

        }


    }

    private fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {taskId ->
                    var location = taskId.result
                    if(location == null){
                        Toast.makeText(this,"Para guardar, espere por las coordenadas",Toast.LENGTH_LONG).show()
                        getNewLocation()
                    }
                    else{
                        Latitude.text = location.latitude.toString()
                        Longitude.text = location.longitude.toString()
                    }

                }
            }
            else{
                Toast.makeText(this,"Se debe activar la localización del dispositivo",Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                        goToMainActivity()
                },2000)
            }
        }
        else{
            RequestPermission()
            Toast.makeText(this,"Se debe activar la localización del dispositivo",Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                goToMainActivity()
            },2000)
        }
    }

    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var location = p0.lastLocation
            /*Coordenadas.text = "Latitud: " + location.latitude + "Longitud: " + location.longitude + " Ciudad" +
                    getCityName(location.latitude,location.longitude) + " País: " + getCountry(location.latitude,location.longitude)*/
            Latitude.text = location.latitude.toString()
            Longitude.text = location.longitude.toString()
        }
    }

    private fun CheckPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            return true
        }
        return false
    }

    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_ID
        )
    }

    private fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getCityName(lat: Double, long: Double): String{
        var CityName = ""
        var geoCoder = Geocoder(this,Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CityName = Adress.get(0).locality
        return CityName
    }

    private fun getCountry(lat: Double, long: Double): String{
        var CountryName = ""
        var geoCoder = Geocoder(this,Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,1)

        CountryName = Adress.get(0).countryName
        return CountryName
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:", "You have the permission")
            }
        }
    }

    private fun saveUser(tipo:String, estado: String, ambiente: String, lugar: String, urlFoto: String, date:String) {

        if((NombreSerp.text.toString() != "") || (TamaSerp.text.toString() != "") || (Observaciones.text.toString() != "") ||
            (Latitude.text.toString() != "") || (Longitude.text.toString() != "")){

            var serpiente = Serpiente(NombreSerp.text.toString(),TamaSerp.text.toString(),urlFoto,tipo,estado,ambiente,
                lugar,Observaciones.text.toString(),date,Latitude.text.toString(),Longitude.text.toString())
//

            val dataBase = FirebaseDatabase.getInstance()
            val myRef = dataBase.getReference("Serpientes")

            myRef.child(NombreSerp.text.toString()).setValue(serpiente)
            Toast.makeText(this,"Avistamiento almacenado",Toast.LENGTH_SHORT).show()
            goToMainActivity()
        }
        else{
            var serpiente = Serpiente("Por defecto","Por defecto",urlFoto,tipo,estado,ambiente,
                lugar,"Por defecto",date,"Por defecto", "Por defecto")

            val dataBase = FirebaseDatabase.getInstance()
            val myRef = dataBase.getReference("Serpientes")

            myRef.child("Por defecto").setValue(serpiente)
            Toast.makeText(this,"Avistamiento almacenado",Toast.LENGTH_SHORT).show()
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            imageViewFoto.setImageBitmap(takenImage)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}