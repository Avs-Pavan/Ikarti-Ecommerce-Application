package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.CartListPojo;
import com.webinfrasolutions.ikarti.Pojo.DashPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 16/12/17.
 */

public interface FetchMyCartListApi {
   @Multipart
    //@POST("/add_favourite.php")
    @POST("/deals/api/Cart/myCartList")
    Call<CartListPojo> fetch(@Part("uid") RequestBody uid
    );

}
