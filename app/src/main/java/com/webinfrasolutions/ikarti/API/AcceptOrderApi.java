package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 28/12/17.
 */

public interface AcceptOrderApi {
    @Multipart
    @POST("/deals/api/Orders/sendOrderAcceptedNotification")
    Call<MyPojo> accept(
            @Part("order_id") RequestBody order_id,
            @Part("uid") RequestBody uid
                        );
}
