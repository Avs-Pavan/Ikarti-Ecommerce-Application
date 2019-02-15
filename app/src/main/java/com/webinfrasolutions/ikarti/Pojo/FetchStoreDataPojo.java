package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 25/12/17.
 */

public class FetchStoreDataPojo  implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("storeKeeper")
    @Expose
    private Storekeeper storeKeeper;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Storekeeper getStoreKeeper() {
        return storeKeeper;
    }

    public void setStoreKeeper(Storekeeper storeKeeper) {
        this.storeKeeper = storeKeeper;
    }

}
