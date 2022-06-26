package com.noor.mystore99.amigrate.ui.upi;

import android.os.Build;
import android.util.Log;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class AutomaticHash {

    //====================================================================================
    // AUTOMATICALLY SIGN
    //====================================================================================
    public static byte[] sign(String algorithms, PrivateKey privateKey, byte[] dataBytes) throws Exception {

        //CREATE SIGNATURE (use Hash first)
        Signature         signature = Signature.getInstance(algorithms);
        signature.initSign(privateKey);
        signature.update(dataBytes);
        byte[]            signatureBytes = signature.sign();

        //DISPLAY ENCODED SIGNATURE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("SIGNATURE", Base64.getEncoder().encodeToString(signatureBytes));
        }

        //RETURN SIGNATURE
        return signatureBytes;

    }

    //====================================================================================
    // AUTOMATICALLY VERIFY
    //====================================================================================
    public static Boolean verify(String algorithms, PublicKey publicKey, byte[] dataBytes, byte[] signatureBytes) throws Exception {

        //INITIALIZE SIGNATURE
        Signature signature = Signature.getInstance(algorithms);
        signature.initVerify(publicKey);
        signature.update(dataBytes);

        //VERIFY SIGNATURE
        boolean   verified = signature.verify(signatureBytes);

        //DISPLAY VERIFICATION
        System.out.println("VERIFIED  = " + verified);

        //RETURN SIGNATURE
        return verified;

    }

}