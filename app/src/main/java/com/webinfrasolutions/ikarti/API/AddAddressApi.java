package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 19/12/17.
 */

public interface AddAddressApi {
    @FormUrlEncoded
    @POST("/deals/api/Address/addAddress")
    Call<MyPojo> add(@Field("uid") String uid,
                     @Field("firstname") String fname,
                     @Field("lastname") String lname,
                     @Field("city") String city,
                     @Field("state") String state,
                     @Field("zipcode") String zipcode,
                     @Field("mobile_number") String number,
                     @Field("address_line") String address
    );
}
