package com.example.emergencyalertapp

import android.content.Intent
import android.net.Uri
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergencyalertapp.ui.theme.EmergencyAlertAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmergencyAlertAppTheme {
                SOSScreen(this)
            }
        }
    }
}

@Composable
fun SOSScreen(activity: ComponentActivity) {

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = {

                val phoneNumber = "7569567348"

                // SMS Permission
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.SEND_SMS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.SEND_SMS),
                        1
                    )
                    return@Button
                }

                // Location Permission
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        2
                    )
                    return@Button
                }

                // Call Permission
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        3
                    )
                    return@Button
                }

                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                val message = if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    "Emergency! I need help. My location: https://maps.google.com/?q=$latitude,$longitude"

                } else {

                    "Emergency! I need help. Location unavailable."
                }

                // Send SMS
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)

                Toast.makeText(context, "SOS Message Sent!", Toast.LENGTH_SHORT).show()

                // Auto Call
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$phoneNumber")
                context.startActivity(callIntent)

            },
            modifier = Modifier.size(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {

            Text(
                text = "SOS",
                fontSize = 30.sp,
                color = Color.White
            )

        }

    }
}