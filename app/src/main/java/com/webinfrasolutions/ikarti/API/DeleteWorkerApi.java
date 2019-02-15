package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 30/12/17.
 */

public interface DeleteWorkerApi {
    @FormUrlEncoded
    @POST("/deals/api/Workers/deleteWorker")
    Call<MyPojo> delete(@Field("wid") String wid);
}
