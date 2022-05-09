package com.noor.mystore99.amigrate.ui.auth

interface OtpReceivedInterface {
    fun onOtpReceived(otp: String)
    fun onOtpTimeout()
}