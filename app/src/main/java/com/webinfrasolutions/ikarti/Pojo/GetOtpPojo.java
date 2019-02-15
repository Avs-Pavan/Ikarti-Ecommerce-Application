package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevin on 25/10/17.
 */

public class GetOtpPojo  implements Serializable{

    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("utype")
    @Expose
    private String utype;
    @SerializedName("newattempt")
    @Expose
    private Boolean newattempt;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("storekeeper")
    @Expose
    private Storekeeper storekeeper;
    @SerializedName("worker")
    @Expose
    private Worker worker;



    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public Boolean getNewattempt() {
        return newattempt;
    }

    public void setNewattempt(Boolean newattempt) {
        this.newattempt = newattempt;
    }

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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Storekeeper getStorekeeper() {
        return storekeeper;
    }

    public void setStorekeeper(Storekeeper storekeeper) {
        this.storekeeper = storekeeper;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }



}