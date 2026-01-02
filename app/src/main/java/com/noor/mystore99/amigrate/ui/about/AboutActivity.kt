package com.noor.mystore99.amigrate.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import com.noor.mystore99.R
import com.noor.mystore99.databinding.ActivityAboutPageBinding

class AboutActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAboutPageBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_page)
        
        setupUI()
    }
    
    private fun setupUI() {
        // Back button
        binding.imgBack.setOnClickListener { finish() }
        
        // Phone card - dial number
        binding.cardPhone.setOnClickListener {
            val phoneNumber = binding.tvPhone.text.toString()
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }
        
        // Email card - send email
        binding.cardEmail.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, "Inquiry from Sabzi Taza App")
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
        
        // WhatsApp card - open WhatsApp chat
        binding.cardWhatsapp.setOnClickListener {
            val phoneNumber = "919117151927" // WhatsApp format with country code
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://wa.me/$phoneNumber")
                }
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback if WhatsApp not installed
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
                }
                startActivity(intent)
            }
        }
    }
}
