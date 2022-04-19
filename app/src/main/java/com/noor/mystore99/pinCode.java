package com.noor.mystore99;

public class pinCode {
   public String pincode,status;

    public pinCode(){

    }

    public pinCode(String pin, String status) {
        this.pincode = pin;
        this.status = status;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
