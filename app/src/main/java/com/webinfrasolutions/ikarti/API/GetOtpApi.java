package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 25/10/17.
 */

public interface GetOtpApi {


    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Login/getotp")
    Call<GetOtpPojo> login(@Part("mobile_number") RequestBody num
    );
}
