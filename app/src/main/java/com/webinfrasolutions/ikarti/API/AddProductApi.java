package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 14/12/17.
 */

public interface AddProductApi {
    @FormUrlEncoded
    @POST("/deals/api/products/newproduct")

    Call<GetOtpPojo> add(@Field("product_name") String product_name,
                         @Field("store_id") String store_id,
                         @Field("product_price") String product_price,
                         @Field("product_description") String product_description,
                         @Field("cat_id") String cat_id,
                         @Field("no_of_products") String no_of_products,
                         @Field("delivery") String deliveryType,
                         @Field("image_list[]") String [] images
    );
}
