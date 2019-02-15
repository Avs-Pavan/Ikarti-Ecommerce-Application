package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.OrderListPojo;
import com.webinfrasolutions.ikarti.Pojo.OtherAndSimilarPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 24/12/17.
 */

public interface FetchSimilarAndOtherApi {
    @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/products/getSimilarAndOtherProducts")
    Call<DashPojo> fetch(@Part("sid") RequestBody sid, @Part("name") RequestBody name
    );
}
