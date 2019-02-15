package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 16/12/17.
 */

public interface AddToCartApi

{
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Cart/addToCart")
    Call<MyPojo> add(

                        @Part("pid") RequestBody pid,
                        @Part("uid") RequestBody uid
    );

}
