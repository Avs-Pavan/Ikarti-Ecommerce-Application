package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 28/12/17.
 */

public interface SeeNOtificationApi {

    @FormUrlEncoded
    @POST("/deals/api/Login/changeNotState")
    Call<MyPojo> change(@Field("not_id") String notId    );
}
