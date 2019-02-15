package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 15/12/17.
 */

public interface MyProductListApi {

    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/myproductlist")
    Call<MyproductsListPojo> fetch(

                        @Part("store_id") RequestBody store_id);
}
