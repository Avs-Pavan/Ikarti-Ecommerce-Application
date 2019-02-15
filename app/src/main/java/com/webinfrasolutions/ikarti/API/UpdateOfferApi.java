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

public interface UpdateOfferApi {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Offer/updateOffer")
    Call<GetOtpPojo> update(@Part("offer_percentage") RequestBody product_name,
                            @Part("store_id") RequestBody store_id,
                            @Part("url") RequestBody url,
                            @Part("id") RequestBody id
    );

}
