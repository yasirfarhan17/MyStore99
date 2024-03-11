package com.noor.mystore99.amigrate.util

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject


open class PushNotification : AppCompatActivity() {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAATe3RKEA:APA91bFvMhJQjFqap-k6nrdfbmQMYdQ-wn676ILUB_L1yox2wd0XRSPrWzdLgyZkYxnctDNPg__FK9X5-CH8Oa3rh84blBxEOAutvakDLGc2EQetx4ecv53vksrOYdLbDdD0zKiKaLRS"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }
    lateinit var titleMsg:String
    lateinit var message:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this,"working",Toast.LENGTH_SHORT).show()
         titleMsg=intent.getStringExtra("title").toString()
        message=intent.getStringExtra("message").toString()
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/SabziTaza")



        if (!TextUtils.isEmpty(titleMsg)) {
            val topic = "/topics/SabziTaza" //topic has to match what the receiver subscribed to

            val notification = JSONObject()
            val notifcationBody = JSONObject()

            try {
                notifcationBody.put("title", "new user register")
                notifcationBody.put("message", "SabziTazaUpadate 1 user register and 2 new order")   //Enter your notification message
                notification.put("to", topic)
                notification.put("data", notifcationBody)
                Log.e("TAG", "try")
            } catch (e: JSONException) {
                Log.e("TAG", "onCreate: " + e.message)
            }

            sendNotification(notification)
        }
    }
    open fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.i("checkingResponse", "onResponse: $response")
//                edtTitle.setText("")
//                edtMessage.setText("")
            },
            Response.ErrorListener {
                Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show()
                Log.i("checkingError", "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
    }
}