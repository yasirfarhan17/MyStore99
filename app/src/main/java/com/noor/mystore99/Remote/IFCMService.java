package com.noor.mystore99.Remote;


import android.database.Observable;

import com.noor.mystore99.Model.FCMResponse;
import com.noor.mystore99.Model.FCMSendData;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/Json",
            "Authorisation:key=AAAATe3RKEA:APA91bFvMhJQjFqap-k6nrdfbmQMYdQ-wn676ILUB_L1yox2wd0XRSPrWzdLgyZkYxnctDNPg__FK9X5-CH8Oa3rh84blBxEOAutvakDLGc2EQetx4ecv53vksrOYdLbDdD0zKiKaLRS"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
