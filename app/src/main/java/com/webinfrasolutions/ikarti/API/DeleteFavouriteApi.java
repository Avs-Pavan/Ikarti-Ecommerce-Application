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

public interface DeleteFavouriteApi {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/deleteFavourite")
    Call<MyPojo> add(@Part("uid") RequestBody uid,
                     @Part("pid") RequestBody pid

    );
}
