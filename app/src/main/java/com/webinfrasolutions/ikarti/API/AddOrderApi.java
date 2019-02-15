package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 21/12/17.
 */

public interface AddOrderApi {
    @FormUrlEncoded
    @POST("/deals/api/Orders/addOrder")
    Call<MyPojo> add(@Field("uid") String uid,
                     @Field("product_id") String productid,
                     @Field("quantity") String quantity,
                     @Field("product_deal_price") String productdealprice,
                     @Field("product_price") String productprice,
                     @Field("saved_price") String savedprice,
                     @Field("item_total") String itemtotal,
                     @Field("store_id") String storeid,
                     @Field("address_id") String addresid,
                     @Field("delivery_type") String deliverytype,
                     @Field("address") String address,
                     @Field("latitude") String latitude,
                     @Field("longitude") String longitude
    );
}
