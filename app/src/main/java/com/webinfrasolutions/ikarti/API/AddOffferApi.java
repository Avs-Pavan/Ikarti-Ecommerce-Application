package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 14/12/17.
 */

public interface AddOffferApi {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Offer/newOffer")
    Call<GetOtpPojo> add(@Part("offers_percentage") RequestBody product_name,

                         @Part("store_id") RequestBody store_id,
                         @Part("url") RequestBody url
    );
}
