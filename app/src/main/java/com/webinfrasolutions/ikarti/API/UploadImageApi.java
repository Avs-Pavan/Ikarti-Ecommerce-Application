package com.webinfrasolutions.ikarti.API;

import com.webinfrasolutions.ikarti.Pojo.UploadIMagePojo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by kevin on 16/12/17.
 */

public interface UploadImageApi {
    @Multipart
    @POST("/deals/api/products/uploadImage")

        //      @POST("/upload_image.php")
    Call<UploadIMagePojo> upload_image(
                                       @Part MultipartBody.Part image);

}
