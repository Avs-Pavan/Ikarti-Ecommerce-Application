package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.CartListPojo;
import com.webinfrasolutions.ikarti.Pojo.FavouriteProductsListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 19/12/17.
 */

public interface FetchFavouriteListApi {

    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/favouriteList")
    Call<FavouriteProductsListPojo> fetch(@Part("uid") RequestBody uid
    );

}
