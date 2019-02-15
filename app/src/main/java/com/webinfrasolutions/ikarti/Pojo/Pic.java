package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 20/12/17.
 */

 public  class Pic implements Serializable {

    @SerializedName("product_pic_id")
    @Expose
    private String productPicId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pic_path")
    @Expose
    private String picPath;
    @SerializedName("status")
    @Expose
    private String status;

    public String getProductPicId() {
        return productPicId;
    }

    public void setProductPicId(String productPicId) {
        this.productPicId = productPicId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
