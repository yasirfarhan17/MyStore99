package com.noor.mystore15.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleAPi {

    @GET
    Call<String> getPath(@Url String  url);
}
