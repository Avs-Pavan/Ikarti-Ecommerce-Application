package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 5/1/18.
 */

public interface WorkerDispatchApi {
    @Multipart
    @POST("/deals/api/Workers/dispatchOrder")

    Call<MyPojo> dispath(
            @Part("oid") RequestBody order_id,
            @Part("uid") RequestBody uid,
            @Part("sid") RequestBody sid,
            @Part("code") RequestBody code
    );
}
