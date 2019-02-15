package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 14/12/17.
 */

public interface RegisterMerchantApi {


    @FormUrlEncoded
    //@POST("/add_favourite.php")
    @POST("/deals/api/users/registermerchant")
    Call<MyPojo> login(@Field("shopname") String num,
                       @Field("ownername") String gg,
                       @Field("contact_number_1") String gggg,
                       @Field("contact_number_2") String ggg,
                       @Field("address") String ggggg,
                       @Field("latitude") String df,
                       @Field("longitude") String dgt,
                       @Field("open_time") String tt,
                       @Field("close_time") String ttt,
                       @Field("category") String holiday_note,
                       @Field("holiday_note") String qqq,
                       @Field("delivery") String delivery,
                       @Field("cat_list[]") String[] qqqf

                          );


}
