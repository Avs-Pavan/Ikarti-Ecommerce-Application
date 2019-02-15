package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 4/1/18.
 */

public class WorkerOrderListPojo  implements Serializable{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("workerorders")
    @Expose
    private List<WorkerOrder> workerorders = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<WorkerOrder> getWorkerorders() {
        return workerorders;
    }

    public void setWorkerorders(List<WorkerOrder> workerorders) {
        this.workerorders = workerorders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
