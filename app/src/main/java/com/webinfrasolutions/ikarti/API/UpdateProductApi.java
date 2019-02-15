package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 15/12/17.
 */

public interface UpdateProductApi {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/updateproduct")
    Call<GetOtpPojo> add(@Part("product_name") RequestBody product_name,
                         @Part("store_id") RequestBody store_id,
                         @Part("product_price") RequestBody product_price,
                         @Part("product_description") RequestBody product_description,
                         @Part("cat_id") RequestBody cat_id,
                         @Part("id") RequestBody id

    );
}
