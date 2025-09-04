package com.example.bottomnavigationbar

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class MeetingDetailTest : Fragment() {

    private val TAG = "MeetingDetailTest"
    private val token = "61|ylWc3ke97mfvANMFsNW4zBucjrJjlui1xDSb6Ez99dc4cefc" // Replace with your token

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_meeting_detail_testing, container, false)
        val webView: WebView = root.findViewById(R.id.webView)

        // Request runtime permissions
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            1
        )

        // WebView settings
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Allow cookies & third-party cookies
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        }

        // Grant camera/mic permissions
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                activity?.runOnUiThread {
                    request?.grant(request?.resources)
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d(TAG, "WebViewConsole: ${consoleMessage?.message()}")
                return true
            }
        }

        // Keep URL loading inside WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean = false

            override fun onPageFinished(view: WebView?, url: String?) {
                // Inject JS to override Laravel Echo authorizer with Bearer token
                val echoAuthJS = """
                    (function waitForEcho() {
    if (window.Echo && window.Echo.connector) {
        console.log("✅ Echo loaded, overriding authorizer");

        window.Echo.connector.options.authorizer = function(channel, options) {
            console.log("🛠️ Authorizer function created for channel:", channel.name);
            return {
                authorize: function(socketId, callback) {
                    console.log("🔥 Authorizer triggered for channel:", channel.name, "socketId:", socketId);
                    fetch("https://staging-meetai.khaleefapps.com/broadcasting/auth", {
                        method: "POST",
                        headers: {
                            "Authorization": "Bearer " + $token,
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            socket_id: socketId,
                            channel_name: channel.name
                        })
                    })
                    .then(response => response.ok ? response.json() : Promise.reject("Forbidden"))
                    .then(data => callback(null, data))
                    .catch(err => callback(err));
                }
            };
        };

        // Subscribe only after override is definitely in place
        window.Echo.private("App.Models.User.3")
            .listen("WebRTCSignal", e => console.log("📩 Event received:", e));

    } else {
        setTimeout(waitForEcho, 50);
    }
})();



                    
                """.trimIndent()
                view?.evaluateJavascript(echoAuthJS, null)
                super.onPageFinished(view, url)
            }
        }

        // Load Laravel page with Bearer token
        val headers = mapOf(
            "Authorization" to "Bearer $token"
        )
        webView.loadUrl("https://staging-meetai.khaleefapps.com/isaac", headers)

        return root
    }

    companion object {
        fun newInstance(meetingId: Int) = MeetingDetailTest().apply {
            arguments = Bundle().apply {
                putInt("MEETING_ID", meetingId)
            }
        }
    }
}
