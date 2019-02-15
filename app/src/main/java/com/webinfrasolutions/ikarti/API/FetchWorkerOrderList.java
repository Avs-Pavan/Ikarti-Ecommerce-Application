package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.OrderListPojo;
import com.webinfrasolutions.ikarti.Pojo.WorkerOrderListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 4/1/18.
 */

public interface FetchWorkerOrderList {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Workers/workerOrderList")
    Call<WorkerOrderListPojo> fetch(@Part("wid") RequestBody wid
    );
}
