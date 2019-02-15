package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.CreateProfilePojo;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 18/12/17.
 */

public interface CreateUserProfileApi {
    @FormUrlEncoded
    //@POST("/add_favourite.php")
    @POST("/deals/api/Login/createprofile")

    Call<CreateProfilePojo> create(@Field("mobile_number") String mobileNumber,
                                  @Field("gender") String gender,
                                  @Field("name") String Username,
                                  @Field("fire_token") String FireToen
    );
}
