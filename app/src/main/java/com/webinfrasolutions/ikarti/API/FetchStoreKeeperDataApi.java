package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.FetchStoreDataPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 15/12/17.
 */

public interface FetchStoreKeeperDataApi {

    @FormUrlEncoded
    @POST("/deals/api/Products/fetchStoreData")
    Call<FetchStoreDataPojo> fetch(@Field("sid") String sid);
}
