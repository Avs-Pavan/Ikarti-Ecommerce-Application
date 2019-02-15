package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyproductsListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 18/12/17.
 */

public interface GetProductsByCat {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/fetchProductsByCat")
    Call<MyproductsListPojo> fetch(

            @Part("cat_id") RequestBody catId);
}
