package com.example.demo

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demo.ui.theme.DemoTheme
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {

    private var fcmToken: String? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleLoginIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Only handle deep link for login info
        handleLoginIntent(intent)

        window.statusBarColor = ContextCompat.getColor(this, R.color.header_yellow)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        // Show basic UI
        setContent {
            DemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    private fun handleLoginIntent(intent: Intent?) {
        Log.d("INTENT", "INTENT:$intent")
        intent?.data?.let { uri ->
            Log.d("URI", "URI: $uri")
            if (uri.scheme == "myapp" && uri.host == "login-success") {
                val userId = uri.getQueryParameter("user_id")?.toIntOrNull()
                val userToken = uri.getQueryParameter("token_auth")

                Log.d("LOGIN", "Received user ID: $userId")
                Log.d("LOGIN", "Received user token: $userToken")

                if (userId != null && !userToken.isNullOrEmpty()) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmToken ->
                        Log.d("FCM", "Obtained FCM token: $fcmToken")
                        Log.d("LOGIN", "userId=$userId, bearerToken=$userToken, fcmToken=$fcmToken")
                        sendTokenToBackend(userId, userToken, fcmToken)
                    }
                } else {
                    Log.e("LOGIN", "Missing user ID or token in deep link")
                }
            }
        }
    }

    fun sendTokenToBackend(userId: Int?, bearerToken: String?, fcmToken: String) {
        if (userId == null || bearerToken.isNullOrEmpty()) return

        val url = "http://10.10.30.183:8000/api/fcm-token/create"

        val json = """
            {   
                "user_id": "$userId",
                "token_fcm": "$fcmToken",
                "token_auth": "$bearerToken"
            }
        """.trimIndent()

        try {
            Log.d("FCM", "Prepared JSON: $json")
        } catch (e: Exception) {
            Log.e("FCM", "Error creating JSON", e)
        }

        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Accept", "application/json")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send to backend", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("FCM", "Response code: ${response.code}")
                Log.d("FCM", "Response from backend: $responseBody")
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            WebView(context).apply {
//                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true


                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.d("DEBUGGGG", "URL: $url")
                        url?.let {
                            if (it.contains("login-success")) {
                                val uri = Uri.parse(it)
                                val userId = uri.getQueryParameter("user_id")?.toIntOrNull()
                                val token = uri.getQueryParameter("token")

                                Log.d("WEBVIEW", "Intercepted login success URL: $url")
                                Log.d("WEBVIEW", "Extracted userId=$userId, token=$token")

                                if (userId != null && !token.isNullOrEmpty()) {
                                    FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmToken ->
                                        Log.d("WEBVIEW", "FCM token: $fcmToken")
                                        sendTokenToBackend(userId, token, fcmToken)
                                    }
                                }
                            }
                        }
                    }
                }



                loadUrl("http://10.10.30.183:8000/login-testing") // or your own URL
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

fun sendTokenToBackend(userId: Int?, bearerToken: String?, fcmToken: String) {
    if (userId == null || bearerToken.isNullOrEmpty()) return

    val url = "http://10.10.30.183:8000/api/fcm-token/create"

    val json = """
            {   
                "user_id": "$userId",
                "token_fcm": "$fcmToken",
                "token_auth": "$bearerToken"
            }
        """.trimIndent()

    try {
        Log.d("FCM", "Prepared JSON: $json")
    } catch (e: Exception) {
        Log.e("FCM", "Error creating JSON", e)
    }

    val body = json.toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
        .url(url)
        .post(body)
        .addHeader("Accept", "application/json")
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FCM", "Failed to send to backend", e)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            Log.d("FCM", "Response code: ${response.code}")
            Log.d("FCM", "Response from backend: $responseBody")
        }
    })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoTheme {
        Greeting("Android")
    }
}
