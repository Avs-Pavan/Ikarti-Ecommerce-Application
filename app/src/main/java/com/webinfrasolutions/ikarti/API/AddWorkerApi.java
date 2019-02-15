package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 30/12/17.
 */

public interface AddWorkerApi {


    @FormUrlEncoded
    //@POST("/add_favourite.php")
    @POST("/deals/api/Workers/addWorker")
    Call<MyPojo> add(@Field("store_id") String storeID,
                     @Field("number") String number,
                     @Field("name") String name

    );
}
