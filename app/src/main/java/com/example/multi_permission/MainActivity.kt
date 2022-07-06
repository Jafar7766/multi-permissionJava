package com.example.multi_permission

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var isText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isText = findViewById(R.id.text1)

        if(!checkPermission()){
            requestPermission()
        }
    }


    private fun checkPermission():Boolean{
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.CAMERA
        )
        return result == PackageManager.PERMISSION_GRANTED
                && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this@MainActivity,arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA
            ),PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted && cameraAccepted) {
                    isText.text = "Permission granted,\nNow you access location data and camera"
                    Toast.makeText(this@MainActivity, "Permission is granted", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       if(shouldShowRequestPermissionRationale(
                               android.Manifest.permission.ACCESS_FINE_LOCATION
                       ) || shouldShowRequestPermissionRationale(
                               android.Manifest.permission.CAMERA)){
                           showMessageOk("You need to allow access to the both permissions"){
                               dialog,_->
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                       requestPermissions(arrayOf(
                                               android.Manifest.permission.ACCESS_FINE_LOCATION,
                                               android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE
                                       )
                                   }
                             }
                           return
                       }
                    }
                }


            }
        }
    }


    private fun showMessageOk(
        message: String,okListener:DialogInterface.OnClickListener
    ){
       AlertDialog.Builder(this@MainActivity)
           .setMessage(message)
           .setPositiveButton("Ok",okListener)
           .create()
           .show()
    }
    companion object{
        private const val PERMISSION_REQUEST_CODE = 200
    }

}