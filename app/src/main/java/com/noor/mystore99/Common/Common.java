package com.noor.mystore99.Common;

import com.noor.mystore99.Remote.GoogleAPi;
import com.noor.mystore99.Remote.RetrofitClient;

public class Common {
    public static final String baseURL="https://maps.googleapis.com";
    public static GoogleAPi getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(GoogleAPi.class);
    }
}
