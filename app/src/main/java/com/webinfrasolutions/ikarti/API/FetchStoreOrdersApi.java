package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.CartListPojo;
import com.webinfrasolutions.ikarti.Pojo.OrderListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 28/12/17.
 */

public interface FetchStoreOrdersApi {

    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Orders/fetchStoreKeeperPendingOrders")
    Call<OrderListPojo> fetch(@Part("sid") RequestBody sid
    );

}
