package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.CategoryListPojo;
import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.OfferListPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 15/12/17.
 */

public interface FetchcategoriesApi {
    @FormUrlEncoded
    @POST("/deals/api/Category/catlist")
    Call<CategoryListPojo> fetch(@Field("offer_id") String id
    );

}
