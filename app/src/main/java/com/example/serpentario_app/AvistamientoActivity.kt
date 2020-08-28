package com.example.serpentario_app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_avistamiento.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE = 3
private lateinit var photoFile: File
class AvistamientoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avistamiento)

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
            val photoRef = storage.reference.child("serpientes").child(NombreSerp.text.toString())
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
                }).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val downloadUri = task.result
                        saveUser(downloadUri.toString())
                    }else{}
                }
        }


    }

    private fun saveUser(urlFoto: String) {
        //var serpiente =
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