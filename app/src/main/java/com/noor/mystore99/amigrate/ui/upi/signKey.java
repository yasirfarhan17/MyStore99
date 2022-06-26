package com.noor.mystore99.amigrate.ui.upi;


import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
public class signKey {





    //====================================================================================
    // SHA256 WITH RSA
    //====================================================================================
    private static void SHA256withRSA(PrivateKey privateKey, PublicKey publicKey, byte[] dataBytes) throws Exception {

        //LOG
        System.out.println("\nSHA256 WITH RSA ================================================================");

        //AUTOMATICALLY SIGN & VERIFY
        System.out.println("\nAUTOMATICALLY SIGN & VERIFY");
        byte[]  automaticSignatureBytes = AutomaticHash.sign  ("SHA256withRSA", privateKey, dataBytes);
        Boolean automaticVerified       = AutomaticHash.verify("SHA256withRSA", publicKey , dataBytes, automaticSignatureBytes);

    }

}
