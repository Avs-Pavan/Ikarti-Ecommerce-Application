package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.AddressListPojo;
import com.webinfrasolutions.ikarti.Pojo.CategoryListPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kevin on 19/12/17.
 */

public interface FetchAddressApi {
    @FormUrlEncoded
    @POST("/deals/api/Address/myAddressList")
    Call<AddressListPojo> fetch(@Field("uid") String id
    );

}
