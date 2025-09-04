package com.example.demo

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody



class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        sendTokenToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.notification?.body}")
    }

    private fun sendTokenToServer(fcmToken: String) {
        val fakeUserId = 123 // Replace with your test user ID
        val fakeTokenAuth = "abc123xyz" // Replace with your test token_auth

        val url = "http://10.0.2.2:8000/api/fcm-token/create"

        val json = JSONObject()
        json.put("user_id", fakeUserId)
        json.put("fcm_token", fcmToken)
        json.put("token_auth", fakeTokenAuth)

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send token: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("FCM", "Response code: ${response.code}")
                Log.d("FCM", "Response from backend: $responseBody")

                if (response.isSuccessful) {
                    Log.i("FCM", "✅ Token sent successfully!")
                    // You can trigger further action here, like navigating to home screen or showing a toast
                } else {
                    Log.w("FCM", "⚠️ Backend responded but not successful: ${response.message}")
                }
            }

        })
    }
}
