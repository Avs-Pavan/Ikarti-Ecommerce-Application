package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 24/12/17.
 */

public class OrderListPojo  implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("orderItem")
    @Expose
    private List<OrderItem> orderItem = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
