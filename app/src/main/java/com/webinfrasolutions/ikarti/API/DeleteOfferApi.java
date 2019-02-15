package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 15/12/17.
 */

public interface DeleteOfferApi {
    @Multipart
    @POST("/deals/api/offer/deleteOffer")
    Call<MyPojo> delete(@Part("offer_id") RequestBody id);
}
