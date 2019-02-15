package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.AddressListPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 27/12/17.
 */

public interface SaveTokenApi {

    @FormUrlEncoded
    @POST("/deals/api/Login/saveToken")
    Call<MyPojo> save(@Field("mobile_number") String number,
                      @Field("token") String token, @Field("type") String utype
    );
}
