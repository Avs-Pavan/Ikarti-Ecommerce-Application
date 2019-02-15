package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.DashPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 15/12/17.
 */

public interface SearchApi {

    @FormUrlEncoded
    @POST("/deals/api/Utils/search")
    Call<DashPojo> fetch(@Field("query") String query);
}
