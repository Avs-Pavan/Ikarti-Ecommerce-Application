package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.AddressListPojo;
import com.webinfrasolutions.ikarti.Pojo.WorkerListPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 30/12/17.
 */

public interface FetchWorkerListApi {
    @FormUrlEncoded
    @POST("/deals/api/Workers/myWorkerList")
    Call<WorkerListPojo> fetch(@Field("sid") String id
    );

}
