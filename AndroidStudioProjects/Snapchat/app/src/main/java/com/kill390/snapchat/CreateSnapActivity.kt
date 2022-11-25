package com.kill390.snapchat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class CreateSnapActivity : AppCompatActivity() {

    lateinit var chooseImageBt:Button
    lateinit var next:Button
    lateinit var snapImage:ImageView
    lateinit var messageEditText: EditText
    var imagename = UUID.randomUUID().toString()+".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)

        chooseImageBt = findViewById(R.id.chooseImageBt)
        messageEditText = findViewById(R.id.messageEditText)
        next = findViewById(R.id.nextBt)
        snapImage = findViewById(R.id.snapImageView)


    }
    fun next (view: View){
        next.isEnabled = false
        var storage = FirebaseStorage.getInstance().getReference().child("Images")
        var ref = storage.child(imagename)
        snapImage.setDrawingCacheEnabled(true)
        snapImage.buildDrawingCache()
        val bitmap = (snapImage.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val uploadTask: UploadTask = ref.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this,"Upload Failed",Toast.LENGTH_SHORT).show()
            next.isEnabled = true
        }.addOnSuccessListener  {
            var dounloadurl = ref.downloadUrl.addOnSuccessListener {uri->
                Log.i("here",uri.toString())
                next.isEnabled = true
                val intent = Intent(this, UsersActivity::class.java)
                intent.putExtra("imageurl",uri.toString())
                intent.putExtra("imagename",imagename)
                intent.putExtra("message",messageEditText.text.toString())
                startActivity(intent)
                Toast.makeText(this,"Upload success",Toast.LENGTH_SHORT).show()
                finish()
            }




        }


    }

    fun chooseImage (view: View){
        chooseImageBt.isEnabled = false
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        chooseImageBt.isEnabled = true
        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                snapImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }
}
