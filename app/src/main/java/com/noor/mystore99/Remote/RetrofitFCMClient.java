package com.noor.mystore99.Remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitFCMClient {
    private static Retrofit instance;
    public static Retrofit getInstance(){
        if(instance==null)
            instance=new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}
